package dev.demo.coindeskDemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Currency {

    @Id
    @Schema(description = "幣別代號", nullable = false, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "幣別代號不可為空")
    private String code;

    @Schema(description = "幣別名稱", nullable = false, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "幣別名稱不可為空")
    private String name;

}
