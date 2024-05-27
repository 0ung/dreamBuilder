import React, { useCallback, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import "bootstrap/dist/css/bootstrap.min.css";
import fetcher from "../fetcher";
import { MANAGE_FILE } from "../constants/api_constants";

interface DropZoneProps {
  filteredFiles: File[];
  setFilteredFiles: (files: File[] | ((prevFiles: File[]) => File[])) => void;
}

interface FilePolicy {
  uploadNum: number;
  uploadSize: number;
  docExtension: string;
  imageExtension: string;
  videoExtension: string;
}

const MyComponent: React.FC<DropZoneProps> = ({
  filteredFiles,
  setFilteredFiles,
}) => {
  const [filePolicy, setFilePolicy] = useState<FilePolicy | null>(null);
  const [fileNum, setFileNum] = useState<number>(0);

  const getFilePolicy = async () => {
    try {
      const response = await fetcher.get(MANAGE_FILE);
      const data = response.data;
      setFilePolicy({
        uploadNum: data.uploadNum,
        uploadSize: data.uploadSize,
        docExtension: data.docExtension,
        imageExtension: data.imageExtension,
        videoExtension: data.videoExtension,
      });
    } catch (error) {
      console.error("Error fetching file policy:", error);
    }
  };

  useEffect(() => {
    getFilePolicy();
  }, []);

  useEffect(() => {
    console.log("Updated filePolicy: ", filePolicy);
  }, [filePolicy]);

  const onDrop = useCallback(
    (acceptedFiles: File[]) => {
      const num = fileNum + acceptedFiles.length;
      if (filePolicy && !handleFileCnt(num)) {
        return;
      }
      const filteredFiles = acceptedFiles.filter((file) => {
        const fileExtension = file.name.split(".").pop()?.toLowerCase() || "";
        return (
          handleFileExtension(fileExtension) &&
          filePolicy &&
          handleFileSize(file.size)
        );
      });
      setFilteredFiles((prevFiles) => [...prevFiles, ...filteredFiles]);
      setFileNum(fileNum + filteredFiles.length);
    },
    [filePolicy, fileNum, filteredFiles]
  );

  const handleFileExtension = (fileExtension: string) => {
    if (filePolicy) {
      const { docExtension, imageExtension, videoExtension } = filePolicy;

      const docExtensionsArray = docExtension.split(",");
      const imageExtensionsArray = imageExtension.split(",");
      const videoExtensionsArray = videoExtension.split(",");

      console.log(docExtensionsArray);
      const isValidExtension =
        docExtensionsArray.includes(fileExtension) ||
        imageExtensionsArray.includes(fileExtension) ||
        videoExtensionsArray.includes(fileExtension);

      if (!isValidExtension) {
        alert("업로드가 금지된 확장자입니다. 다시 업로드 해주세요");
      }
      return isValidExtension;
    }
    return false;
  };

  const handleFileSize = (filesize: number) => {
    if (filePolicy && filesize > filePolicy.uploadSize) {
      alert(
        `파일 사이즈가 ${filePolicy.uploadSize / 1024}KB 를 넘을 수 없습니다.`
      );
      return false;
    }
    return true;
  };

  const handleFileCnt = (num: number) => {
    if (filePolicy) {
      if (num > filePolicy.uploadNum) {
        alert(
          `파일 업로드 개수가 ${filePolicy.uploadNum}개를 넘을 수 없습니다.`
        );
        return false;
      }
    }
    return true;
  };

  const clearDropZone = () => {
    setFilteredFiles([]);
    setFileNum(0);
  };

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  const files = filteredFiles.map((file) => (
    <li key={file.name} className="list-group-item">
      {file.name}
    </li>
  ));

  const dropzoneStyle: React.CSSProperties = {
    border: "2px dashed #007bff",
    padding: "20px",
    borderRadius: "5px",
    textAlign: "center",
    backgroundColor: isDragActive ? "#e9ecef" : "#f8f9fa",
    transition: "background-color 0.3s ease",
  };

  return (
    <div className="container mt-3 mb-3">
      <h3 className="mb-3">파일 업로드</h3>
      <div
        {...getRootProps({ className: "dropzone" })}
        style={dropzoneStyle as React.CSSProperties}
      >
        <input {...getInputProps()} />
        {isDragActive ? (
          <p className="text-primary">Drop the files here ...</p>
        ) : (
          <p className="text-secondary">드래그로 파일을 업로드 하세요!</p>
        )}
      </div>
      <aside>
        <h4 className="mt-4">업로드된 파일</h4>
        <ul className="list-group">{files}</ul>
      </aside>
      <button
        className="mt-2 btn btn-primary"
        style={{
          backgroundColor: " #348f8f",
          border: "none",
          color: "white",
        }}
        onClick={clearDropZone}
      >
        파일 초기화
      </button>
    </div>
  );
};

export default MyComponent;
