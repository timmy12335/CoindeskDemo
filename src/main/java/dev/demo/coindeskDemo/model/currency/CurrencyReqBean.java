package dev.demo.coindeskDemo.model.currency;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyReqBean {

    @Schema(description = "幣別代號", nullable = false, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "幣別代號不可為空")
    private String code;

    @Schema(description = "幣別名稱", nullable = false, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "幣別名稱不可為空")
    private String name;
}
