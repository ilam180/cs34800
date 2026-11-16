package com.example.demo.dto;

//the json body for claiming, e.g. {"engineerId": 5}
public record ClaimRequest(Integer engineerId) {
}