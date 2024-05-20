import React, { ReactNode, useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import "bootstrap/dist/css/bootstrap.min.css";
import { image } from "@uiw/react-md-editor";

interface DropZoneProps {
  filteredFiles: File[];
  setFilteredFiles: (files: File[]) => void;
}
const MyComponent: React.FC<DropZoneProps> = ({
  filteredFiles,
  setFilteredFiles,
}) => {
  const [filePolicy, setFilePolicy] = useState({
    fileCount: 2,
    fileSize: 150000000,
    fileExtensions: [
      { type: "file", extensions: ["html", "pdf", "asdasd"] },
      { type: "image", extensions: ["png", "svg", "jpg"] },
      { type: "video", extensions: ["mp4", "mp5"] },
    ],
  });

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (!handleFileCnt(acceptedFiles)) {
      return;
    }
    const filteredFiles = acceptedFiles.filter((file) => {
      const fileExtension = file.name.split(".").pop()?.toLowerCase() || "";
      return handleFileExtension(fileExtension) && handleFileSize(file.size);
    });
    setFilteredFiles(filteredFiles);
    console.log(filteredFiles);
  }, []);

  const handleFileExtension = (fileExtension: string) => {
    const isValidExtension = filePolicy.fileExtensions.some((policy) =>
      policy.extensions.includes(fileExtension)
    );
    if (!isValidExtension) {
      alert("업로드가 금지된 확장자입니다. 다시 업로드 해주세요");
    }
    return isValidExtension;
  };

  const handleFileSize = (filesize: number) => {
    if (filesize > filePolicy.fileSize) {
      alert(`파일 사이즈가 ${filePolicy.fileSize}KB 를 넘을 수 없습니다.`);
      return false;
    }
    return true;
  };

  const handleFileCnt = (file: File[]) => {
    if (file.length > filePolicy.fileCount) {
      alert(`파일 업로드 개수가 ${filePolicy.fileCount}개를 넘을 수 없습니다.`);
      return false;
    }
    return true;
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
    <div className="container mt-3">
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
    </div>
  );
};

export default MyComponent;
