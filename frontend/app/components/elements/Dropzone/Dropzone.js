import React, { useCallback, useRef, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import styles from './Dropzone.module.css';
import { RiImageAddLine } from 'react-icons/ri';
import Button from '../Button/Button';
import { Cropper } from 'react-cropper';
import { useController } from 'react-hook-form';
import Image from 'next/image';

function Dropzone({ name, control, displayedImage }) {
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
      maxSize: 5000000,
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
          height={9 * 50}
          width={16 * 50}
          alt="highlight photo"
        />
      )}
      <RiImageAddLine size="3rem" color="lightgray" />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : !image ? (
        <>
          <p>Drag and drop to upload</p>
          <Button type="button" size="md" appearance="white" onClick={open}>
            Pick a Highlight Photo
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
            aspectRatio={16 / 9}
            initialAspectRatio={16 / 9}
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
          <p>or</p>
          <Button
            type="button"
            size="md"
            appearance="danger"
            onClick={() => {
              setImage(null);
              field.onChange(null);
            }}
          >
            Remove the Highlight Photo
          </Button>
        </>
      )}
      <p>JPEG or PNG, no larger than 5MB at least 1200x675 pixels</p>
      {fileRejectionItems}
    </div>
  );
}

export default Dropzone;
