import React, { useEffect, useState } from "react";
import Header from "../layout/Header";
import Footer from "../layout/Footer";
import fetcher from "../fetcher";
import { MANAGE_FILE, MANAGE_FILE_POST } from "../constants/api_constants";

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
  const initialExtensions: Extension = {
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
  };

  const [maxSize, setMaxSize] = useState<string>();
  const [maxFileCnt, setMaxFileCnt] = useState<string>();
  const [videoExtension, setVideoExtension] = useState<string>("");
  const [imageExtension, setImageExtension] = useState<string>("");
  const [docsExtension, setDocsExtension] = useState<string>();

  const parseExtensions = (extensions: string) => {
    console.log(videoExtension + imageExtension + docsExtension);
    if (extensions == null || extensions == undefined) {
      return;
    }
    const extArray = extensions.split(",");
    const extObject: { [key: string]: boolean } = {};
    extArray.forEach((ext) => {
      extObject[ext] = true;
    });
    return extObject;
  };

  const [extension, setExtension] = useState<Extension>(initialExtensions);

  useEffect(() => {
    const getFileUploadData = async () => {
      const response = await fetcher.get(MANAGE_FILE);
      setMaxFileCnt(response.data.uploadNum);
      setMaxSize(response.data.uploadSize);
      setVideoExtension(response.data.videoExtension);
      setImageExtension(response.data.imageExtension);
      setDocsExtension(response.data.docExtension);
      setExtension({
        video: {
          ...initialExtensions.video,
          ...parseExtensions(response.data.videoExtension),
        },
        image: {
          ...initialExtensions.image,
          ...parseExtensions(response.data.imageExtension),
        },
        docs: {
          ...initialExtensions.docs,
          ...parseExtensions(response.data.docExtension),
        },
      });
    };

    getFileUploadData();
  }, []);

  const handleCheckboxChange = (category: keyof Extension, ext: string) => {
    setExtension((prevExtension) => ({
      ...prevExtension,
      [category]: {
        ...prevExtension[category],
        [ext]: !prevExtension[category][ext],
      },
    }));
  };

  const convertExtensionsToString = (extensions: {
    [key: string]: boolean;
  }) => {
    return Object.keys(extensions)
      .filter((key) => extensions[key])
      .join(",");
  };

  const handleFileUpload = async () => {
    const data = {
      uploadSize: maxSize,
      uploadNum: maxFileCnt,
      docExtension: convertExtensionsToString(extension.docs),
      imageExtension: convertExtensionsToString(extension.image),
      videoExtension: convertExtensionsToString(extension.video),
    };
    try {
      await fetcher.post(MANAGE_FILE_POST, JSON.stringify(data), {
        headers: {
          "Content-Type": "application/JSON",
        },
      });
      alert("등록이 완료되었습니다.");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <Header />
      <div className="container mt-5">
        <h1>파일 업로드 관리</h1>
        <hr />
        <div className="form-floating mb-3">
          <input
            required
            type="text"
            className="form-control"
            id="floatingFileSize"
            placeholder="파일업로드"
            value={maxSize}
            onChange={(e) => {
              setMaxSize(e.target.value);
            }}
          />
          <label htmlFor="floatingFileSize">
            파일 최대 업로드 용량 ex) 20kb, 20mb / 200MB 초과 금지
          </label>
        </div>
        <div className="form-floating">
          <input
            required
            type="number"
            className="form-control"
            id="floatingFileCount"
            placeholder="파일개수"
            min={0}
            max={100}
            value={maxFileCnt}
            onChange={(e) => {
              setMaxFileCnt(e.target.value);
            }}
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
          <button className="btn btn-primary" onClick={handleFileUpload}>
            제출
          </button>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default ManageFileUpload;
