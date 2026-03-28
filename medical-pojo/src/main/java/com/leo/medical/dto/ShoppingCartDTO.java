package com.leo.medical.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long doctorId;
    private Long checkupPackageId;
    private String doctorFlavor;

}
