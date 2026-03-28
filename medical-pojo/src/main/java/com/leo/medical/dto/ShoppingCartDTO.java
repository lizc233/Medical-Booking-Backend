package com.leo.medical.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long doctorId;
    private Long setmealId;
    private String doctorFlavor;

}
