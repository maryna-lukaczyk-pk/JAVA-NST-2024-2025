package com.example.projectmanagerapp.integration.utilities;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

public class AssertionHelper {
    @SneakyThrows
    public static void AssertIsEmpty(MvcResult mvcResult) {
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.isEmpty());
    }
}
