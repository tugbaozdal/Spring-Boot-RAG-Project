package com.narveri.narveri.service;

import java.util.Map;

public interface TemplateService {
    String createTemplate(Map<String, String> dataModel, String templateName);

}
