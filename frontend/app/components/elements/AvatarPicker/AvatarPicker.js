import React, { useCallback, useRef, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import styles from './AvatarPicker.module.css';
import { RiImageAddLine } from 'react-icons/ri';
import Button from '../Button/Button';
import { Cropper } from 'react-cropper';
import { useController } from 'react-hook-form';
import Image from 'next/image';

function AvatarPicker({ name, control, displayedImage, saveImage }) {
  const { field } = useController({ name: name, control: control });
  const [image, setImage] = useState(null);
  const cropperRef = useRef(null);

  const onDropAccepted = useCallback((acceptedFiles) => {
    const reader = new FileReader();
    reader.addEventListener(
      'load',
      function () {
        // convert image file to base64 string
        setImage(reader.result);
      },
      false
    );
    reader.readAsDataURL(acceptedFiles[0]);
  }, []);

  const { getRootProps, getInputProps, isDragActive, fileRejections, open } =
    useDropzone({
      onDropAccepted,
      noClick: true,
      maxFiles: 1,
      maxSize: 3000000,
      accept: 'image/jpeg, image/png',
    });

  const fileRejectionItems = fileRejections.map(({ file, errors }) => (
    <li key={file.name}>
      <ul>
        {errors.map((e) => (
          <li className="text-red-600" key={e.code}>
            {e.message}
          </li>
        ))}
      </ul>
    </li>
  ));

  return (
    <div className={styles.container} {...getRootProps()}>
      <input {...getInputProps()} />
      {displayedImage && !image && (
        <Image
          src={displayedImage}
          height={200}
          width={200}
          alt="profile photo"
          className="rounded-full"
        />
      )}
      <RiImageAddLine size="3rem" color="lightgray" />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : !image ? (
        <>
          <p>Drag and drop to upload</p>
          <Button type="button" size="lg" appearance="primary" onClick={open}>
            Upload Avatar
          </Button>
        </>
      ) : (
        <>
          <Cropper
            name={name}
            src={image}
            style={{
              height: 400,
              width: '100%',
            }}
            // Cropper.js options
            aspectRatio={1}
            initialAspectRatio={1}
            viewMode={1}
            minCropBoxHeight={10}
            minCropBoxWidth={10}
            background={false}
            responsive={true}
            autoCropArea={1}
            checkOrientation={false}
            guides={true}
            ref={cropperRef}
            ready={() => {
              cropperRef.current.cropper.getCroppedCanvas().toBlob((blob) => {
                field.onChange(blob);
              });
            }}
            cropend={() => {
              cropperRef.current.cropper.getCroppedCanvas().toBlob((blob) => {
                field.onChange(blob);
              });
            }}
          />
        </>
      )}
      <p>JPEG or PNG, no larger than 3MB at least 128x128 pixels</p>
      {fileRejectionItems}
      {image && (
        <div className="w-full flex gap-1 justify-center">
          <Button
            type="button"
            size="lg"
            appearance="danger"
            onClick={() => {
              setImage(null);
              field.onChange(null);
            }}
          >
            Cancel
          </Button>
          <Button
            type="button"
            appearance="success"
            size="lg"
            onClick={saveImage}
          >
            Save
          </Button>
        </div>
      )}
    </div>
  );
}

export default AvatarPicker;
