package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.FileRecord;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.UUID;

@Mapper
public interface FileRecordConverter {


    FileRecordConverter INSTANCE = Mappers.getMapper(FileRecordConverter.class);

    default FileRecord toCreate(String userCode, String fileName, String fileUrl) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setUserId(userCode);
        fileRecord.setRecordId(UUID.randomUUID().toString());
        fileRecord.setFileName(fileName);
        fileRecord.setFileUrl(fileUrl);
        fileRecord.setCreateTime(new Date());
        fileRecord.setCreator(userCode);
        return fileRecord;
    }

    default FileRecordVo toFileRecordVo(FileRecord fileRecord) {
        FileRecordVo fileRecordVo = new FileRecordVo();
        fileRecordVo.setRecordId(fileRecord.getRecordId());
        fileRecordVo.setFileUrl(fileRecord.getFileUrl());
        fileRecordVo.setFileName(fileRecord.getFileName());
        return fileRecordVo;
    }
}
