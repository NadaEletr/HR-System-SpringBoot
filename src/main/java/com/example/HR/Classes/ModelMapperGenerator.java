package com.example.HR.Classes;

import org.modelmapper.ModelMapper;

public class ModelMapperGenerator {
    static ModelMapper modelMapper = null;

    public static ModelMapper getModelMapperSingleton() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
        }
        return modelMapper;
    }
}
