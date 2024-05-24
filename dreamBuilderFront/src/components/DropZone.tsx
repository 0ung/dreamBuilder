import React, { ReactNode, useCallback, useEffect, useState } from "react";
import { useDropzone } from "react-dropzone";
import "bootstrap/dist/css/bootstrap.min.css";
import { image } from "@uiw/react-md-editor";
import fetcher from "../fetcher";
import { MANAGE_FILE } from "../constants/api_constants";

interface DropZoneProps {
  filteredFiles: File[];
  setFilteredFiles: (files: File[]) => void;
}

const MyComponent: React.FC<DropZoneProps> = ({
  filteredFiles,
  setFilteredFiles,
}) => {
  const [filePolicy, setFilePolicy] = useState({
    uploadNum: 2,
    uploadSize: 150000000,
    docExtension: "",
    imageExtension: "",
    videoExtension: "",
  });

  const getFilePolicy = async () => {
    const response = await fetcher.get(MANAGE_FILE);
    const data = response.data;
    setFilePolicy({
      uploadNum: data.uploadNum,
      uploadSize: data.uploadSize,
      docExtension: data.docExtension,
      imageExtension: data.imageExtension,
      videoExtension: data.videoExtension,
    });
  };

  useEffect(() => {
    getFilePolicy();
  }, []);

  useEffect(() => {
    console.log("Updated filePolicy: ", filePolicy);
  }, [filePolicy]);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (!handleFileCnt(acceptedFiles)) {
      return;
    }
    const filteredFiles = acceptedFiles.filter((file) => {
      const fileExtension = file.name.split(".").pop()?.toLowerCase() || "";
      return handleFileExtension(fileExtension) && handleFileSize(file.size);
    });
    setFilteredFiles((prevFiles) => [...prevFiles, ...filteredFiles]);
    console.log(filteredFiles);
  }, []);

  const handleFileExtension = (fileExtension: string) => {
    console.log(filePolicy);

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
  };

  const handleFileSize = (filesize: number) => {
    if (filesize > filePolicy.uploadSize) {
      alert(`파일 사이즈가 ${filePolicy.uploadSize}KB 를 넘을 수 없습니다.`);
      return false;
    }
    return true;
  };

  const handleFileCnt = (file: File[]) => {
    if (file.length > filePolicy.uploadNum) {
      alert(`파일 업로드 개수가 ${filePolicy.uploadNum}개를 넘을 수 없습니다.`);
      return false;
    }
    return true;
  };

  const clearDropZone = () => {
    setFilteredFiles([]);
  };
  const { getRootProps, getInputProps, isDragActive, acceptedFiles } =
    useDropzone({ onDrop });

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
