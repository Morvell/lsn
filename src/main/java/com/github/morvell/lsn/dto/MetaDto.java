package com.github.morvell.lsn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Андрей Захаров
 * @created 26/04/2020
 */
@AllArgsConstructor
@Data
public class MetaDto {

    private String title;

    private String description;

    private String cover;
}
