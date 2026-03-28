package com.leo.medical.controller;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leo.medical.entity.Department;
import com.leo.medical.entity.Doctor;
import com.leo.medical.mapper.DepartmentMapper;
import com.leo.medical.mapper.DoctorMapper;
import com.leo.medical.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "智能代理")
@RequestMapping("/ai")
public class AiAgentController {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DoctorMapper doctorMapper;

    // 通过 @Value 从 yml 文件动态读取配置
    @Value("${medical.ai.deepseek.api-key}")
    private String apiKey;
    @Value("${medical.ai.deepseek.api-url}")
    private String apiUrl;

    @PostMapping("/triage")
    @ApiOperation("症状分析与智能分诊")
    public Result<JSONObject> triage(@RequestBody String patientSymptom){
        try{
            //1.获取数据库科室数据
            List<Department> deptList=departmentMapper.list(1);
            List<String> deptNames=deptList.stream().map(Department::getName).collect(Collectors.toList());
            // 2. 构造强大的 Prompt
            //String prompt_system = "你是一个三甲医院的资深分诊护士。";
            String prompt_user = " 患者症状是：【" + patientSymptom + "】。" +
                    "请根据症状，从以下本院科室列表中选择最合适的一个科室：" + deptNames.toString() + "。" +
                    "请务必只输出一个纯JSON对象，不要包含任何Markdown格式或额外文字。格式要求：{\"departmentName\": \"推荐科室名\", \"reason\": \"一句话解释原因\"}";

            // 3. 调用 DeepSeek API
            String aiResponseJson = callDeepSeekApi(prompt_user);
            log.info("DeepSeek 原始返回: {}", aiResponseJson);
            // 4. 解析大模型返回的 JSON
            JSONObject aiResult = JSON.parseObject(aiResponseJson);
            String targetDeptName = aiResult.getString("departmentName");

            aiResult.put("recommendedDepartment", targetDeptName);

            // 5. 联动数据库（Agent Action）：查询该科室下的医生+ Limit 1
            Department targetDept = departmentMapper.getByNameAndDepartemnt(targetDeptName);
            if (targetDept != null) {
                List<Doctor> doctors = doctorMapper.getByDeptId(targetDept.getId());
                aiResult.put("recommendedDoctors", doctors); // 把医生列表塞进返回结果里
            } else {
                aiResult.put("recommendedDoctors", new JSONArray());
            }

            return Result.success(aiResult);
        }catch (Exception e) {
            /*log.error("AI 导诊服务异常: ", e);
            return Result.error("AI 导诊服务开小差了，请稍后重试或直接前往挂号大厅。");*/
            JSONObject errObj = new JSONObject();
            errObj.put("recommendedDepartment", "后端代码报错啦！");
            errObj.put("departmentName", "后端代码报错啦！");
            errObj.put("reason", "真实报错信息是：" + e.toString());
            errObj.put("recommendedDoctors", new JSONArray());

            return Result.success(errObj);
        }
    }
    /**
     * 发送 HTTP POST 请求给 DeepSeek
     */
    private String callDeepSeekApi(String prompt) {
        boolean isMock = true; // 由于没充值，所以设为 true，直接拦截请求，返回假数据
        if (isMock) {
            log.info("触发本地 Mock 挡板，不消耗真实 API 额度...");
            // 直接返回一段符合我们要求格式的假 JSON 给上层去解析
            return "{\"departmentName\": \"内科\", \"reason\": \"根据您的症状，建议先前往内科进行基础排查。\"}";
        }
        // 按照 DeepSeek/OpenAI 接口规范组装请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat"); // 使用 deepseek 聊天模型

        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();

        message.put("role", "system");
        message.put("content", "你是一个三甲医院的资深分诊护士。");
        messages.add(message);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.3); // 降低温度值，让回答更严谨（医疗场景适用）

        // 使用 Hutool 发送请求
        String result = HttpRequest.post(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toJSONString())
                .execute()
                .body();

        // 解析 DeepSeek 返回的深层嵌套 JSON：响应体 -> choices -> [0] -> message -> content
        JSONObject responseObj = JSON.parseObject(result);
        return responseObj.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
}
