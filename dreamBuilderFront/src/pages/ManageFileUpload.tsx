import React, { useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";

interface Extension {
  video: { [key: string]: boolean };
  image: { [key: string]: boolean };
  docs: { [key: string]: boolean };
}

interface ExtensionCheckboxGroupProps {
  category: string;
  extensions: { [key: string]: boolean };
  onCheckboxChange: (category: keyof Extension, ext: string) => void;
}

const ExtensionCheckboxGroup: React.FC<ExtensionCheckboxGroupProps> = ({
  category,
  extensions,
  onCheckboxChange,
}) => {
  return (
    <div className="mb-4">
      <h4 className="text-primary">
        {category.charAt(0).toUpperCase() + category.slice(1)} 확장자
      </h4>
      <div className="card">
        <div className="card-body">
          <div className="d-flex flex-wrap">
            {Object.keys(extensions).map((ext) => (
              <div className="form-check form-check-inline" key={ext}>
                <input
                  className="form-check-input"
                  type="checkbox"
                  checked={extensions[ext]}
                  onChange={() =>
                    onCheckboxChange(category as keyof Extension, ext)
                  }
                  id={`${category}-${ext}`}
                />
                <label
                  className="form-check-label"
                  htmlFor={`${category}-${ext}`}
                >
                  {ext}
                </label>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

function ManageFileUpload() {
  const [extension, setExtension] = useState<Extension>({
    video: {
      mp4: false,
      avi: false,
      mkv: false,
      mov: false,
      wmv: false,
      flv: false,
      webm: false,
      mpeg: false,
      mpg: false,
      "3gp": false,
    },
    image: {
      jpg: false,
      jpeg: false,
      png: false,
      gif: false,
      bmp: false,
      tiff: false,
      svg: false,
      webp: false,
      ico: false,
      heic: false,
    },
    docs: {
      pdf: false,
      docx: false,
      xlsx: false,
      pptx: false,
      txt: false,
      rtf: false,
      odt: false,
      ods: false,
      odp: false,
      csv: false,
      hwp: false,
    },
  });

  const handleCheckboxChange = (category: keyof Extension, ext: string) => {
    setExtension((prevExtension) => ({
      ...prevExtension,
      [category]: {
        ...prevExtension[category],
        [ext]: !prevExtension[category][ext],
      },
    }));
  };

  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>파일 업로드 관리</h1>
        <hr />
        <div className="form-floating mb-3">
          <input
            type="text"
            className="form-control"
            id="floatingFileSize"
            placeholder="파일업로드"
          />
          <label htmlFor="floatingFileSize">
            파일 최대 업로드 용량 ex) 20kb, 20mb / 200MB 초과 금지
          </label>
        </div>
        <div className="form-floating">
          <input
            type="number"
            className="form-control"
            id="floatingFileCount"
            placeholder="파일개수"
            min={0}
            max={100}
          />
          <label htmlFor="floatingFileCount">파일 최대 개수</label>
        </div>
        <hr className="mt-3" />
        <div className="mt-3">
          <ExtensionCheckboxGroup
            category="video"
            extensions={extension.video}
            onCheckboxChange={handleCheckboxChange}
          />
          <ExtensionCheckboxGroup
            category="image"
            extensions={extension.image}
            onCheckboxChange={handleCheckboxChange}
          />
          <ExtensionCheckboxGroup
            category="docs"
            extensions={extension.docs}
            onCheckboxChange={handleCheckboxChange}
          />
        </div>
        <div className="d-flex justify-content-end">
          <button className="btn btn-primary">제출</button>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default ManageFileUpload;
