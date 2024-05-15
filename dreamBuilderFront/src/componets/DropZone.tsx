import React, { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import "bootstrap/dist/css/bootstrap.min.css";

const MyComponent: React.FC = () => {

  const [extensions,setExtensions] = useState<String[]>([
    "hwp" , "pdf" , "png"
  ]);
  const [fileSize,setFileSize] = useState();


  const onDrop = useCallback((acceptedFiles: File[]) => {
    // 파일을 처리하는 로직을 여기에 추가하세요
    acceptedFiles.map((e)=>{
      if(e.name)
    })
    console.log(acceptedFiles);
  }, []);

  const handleFileExtension = (extension: string)=>{
      
  }

  const handleFileSize = (extension: number)=>{

  }
  const { getRootProps, getInputProps, isDragActive, acceptedFiles } =
    useDropzone({ onDrop });

  const files = acceptedFiles.map((file) => (
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
