package com.diudiu.diudiu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 给世界一点小小的二次元震撼
 *
 *  ██   ██         ███████   ████     ██
 * ░██  ██         ██░░░░░██ ░██░██   ░██
 * ░██ ██         ██     ░░██░██░░██  ░██
 * ░████    █████░██      ░██░██ ░░██ ░██
 * ░██░██  ░░░░░ ░██      ░██░██  ░░██░██
 * ░██░░██       ░░██     ██ ░██   ░░████
 * ░██ ░░██       ░░███████  ░██    ░░███
 * ░░   ░░         ░░░░░░░   ░░      ░░░
 *
 * @Author : Drme
 * @Date : 2024/02/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixLogDto {
    private Integer id;
    private String adminName;
    private String userName;
    private String state;
    private String description;
    private String createTime;
    private String takeTime;
    private String finishTime;
}
