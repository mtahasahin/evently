import React, { useEffect, useRef } from 'react';
import { useController } from 'react-hook-form';

function Editor({ editorLoaded, name, control }) {
  const editorRef = useRef();
  const { CKEditor, ClassicEditor } = editorRef.current || {};
  const { field } = useController({ control, name });

  useEffect(() => {
    editorRef.current = {
      CKEditor: require('@ckeditor/ckeditor5-react').CKEditor, // v3+
      ClassicEditor: require('@ckeditor/ckeditor5-build-classic'),
    };
  }, []);

  return (
    <div className="w-full">
      {editorLoaded ? (
        <CKEditor
          type=""
          name={name}
          editor={ClassicEditor}
          data={field.value}
          config={{
            toolbar: [
              'heading',
              '|',
              'bold',
              'italic',
              'link',
              'bulletedList',
              'numberedList',
            ],
          }}
          onReady={(editor) => {
            editor.setData(field.value);
          }}
          onChange={(event, editor) => {
            const data = editor.getData();
            field.onChange(data);
          }}
        />
      ) : (
        <div>Editor loading</div>
      )}
    </div>
  );
}

export default Editor;
