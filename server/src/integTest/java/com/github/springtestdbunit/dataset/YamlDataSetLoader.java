package com.github.springtestdbunit.dataset;

import org.dbunit.dataset.IDataSet;
import org.dbunit.yaml.YamlDataSet;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class YamlDataSetLoader extends AbstractDataSetLoader {
    @Override
    protected IDataSet createDataSet(final Resource resource) throws Exception {
        try (InputStream inputStream = resource.getInputStream()) {
            return new YamlDataSet(inputStream);
        }
    }
}
