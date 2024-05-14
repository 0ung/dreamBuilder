import React, { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";

const dropzoneStyle: React.CSSProperties = {
  border: "2px dashed #cccccc",
  borderRadius: "5px",
  padding: "20px",
  textAlign: "center",
  cursor: "pointer",
};

export default function MyDropzone() {
  const [files, setFiles] = useState<File[]>([]);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    setFiles(acceptedFiles);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div className="container mt-5">
      <span>파일 업로드</span>
      <div {...getRootProps({ className: "dropzone" })} style={dropzoneStyle}>
        <input {...getInputProps()} />
        {isDragActive ? (
          <p>Drop the files here ...</p>
        ) : (
          <p>Drag 'n' drop some files here, or click to select files</p>
        )}
      </div>
      <aside>
        <h4>Files</h4>
        <ul>
          {files.map((file) => {
            let data = file.name;
            return <li key={file.name}>{file.name}</li>;
          })}
        </ul>
      </aside>
    </div>
  );
}
