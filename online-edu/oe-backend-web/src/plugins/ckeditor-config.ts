/**
 * This configuration was generated using the CKEditor 5 Builder. You can modify it anytime using this link:
 * https://ckeditor.com/ckeditor-5/builder/#installation/NoNgNARATAdAzPCkAsc4AYAcUQEZ1wggECsIyUJJyAnKriNQOxU0hPGaZIQBeAFgFoAxgDsk6MMFxhJkmfIC6kdACN2AQyhMIioA
 */
import 'ckeditor5/ckeditor5.css';
import '@/style/ckeditor.css';

import {
  Alignment,
  Autoformat,
  AutoImage,
  Autosave,
  BlockQuote,
  Bold,
  ClassicEditor,
  Emoji,
  Essentials,
  GeneralHtmlSupport,
  Heading,
  HorizontalLine,
  ImageBlock,
  ImageCaption,
  ImageInline,
  ImageInsert,
  ImageInsertViaUrl,
  ImageResize,
  ImageStyle,
  ImageTextAlternative,
  ImageToolbar,
  ImageUpload,
  Indent,
  IndentBlock,
  Italic,
  Link,
  LinkImage,
  List,
  ListProperties,
  MediaEmbed,
  Mention,
  Paragraph,
  PasteFromOffice,
  SimpleUploadAdapter,
  SourceEditing,
  Strikethrough,
  Table,
  TableCaption,
  TableCellProperties,
  TableColumnResize,
  TableProperties,
  TableToolbar,
  TextTransformation,
  TodoList,
  Underline,
} from 'ckeditor5';
import translations from 'ckeditor5/translations/zh-cn.js';
import type { Plugin } from 'vue';
import { computed, ref } from 'vue';

import { TOKEN_NAME } from '@/config/global';

// const LICENSE_KEY =
// 	'eyJhbGciOiJFUzI1NiJ9.eyJleHAiOjE3NDI5NDcxOTksImp0aSI6IjBhMWYxZWM2LWIyOTEtNDZlYS1hOTNiLTQ4MDZhODBkYzBjOCIsInVzYWdlRW5kcG9pbnQiOiJodHRwczovL3Byb3h5LWV2ZW50LmNrZWRpdG9yLmNvbSIsImRpc3RyaWJ1dGlvbkNoYW5uZWwiOlsiY2xvdWQiLCJkcnVwYWwiLCJzaCJdLCJ3aGl0ZUxhYmVsIjp0cnVlLCJsaWNlbnNlVHlwZSI6InRyaWFsIiwiZmVhdHVyZXMiOlsiKiJdLCJ2YyI6ImQ3NmQwMzgwIn0.hlltiIneqmEohzlXhqMXelQUglnQgDFHnT_brKMtcn2TZ0uA3f-VqXEv6VXNE9eghFSh-COsCCZQeJmSz2_3Sw';

const tokenInfo = ref(localStorage.getItem(TOKEN_NAME) || '');

const LICENSE_KEY = 'GPL';

const isLayoutReady = ref(true);

const editor = ClassicEditor;

const config = computed(() => {
  if (!isLayoutReady.value) {
    return null;
  }

  return {
    toolbar: {
      items: [
        'sourceEditing',
        '|',
        'heading',
        '|',
        'bold',
        'italic',
        'underline',
        'strikethrough',
        '|',
        'emoji',
        'horizontalLine',
        'link',
        'insertImage',
        'mediaEmbed',
        'insertTable',
        'blockQuote',
        '|',
        'alignment',
        '|',
        'bulletedList',
        'numberedList',
        'todoList',
        'outdent',
        'indent',
      ],
      shouldNotGroupWhenFull: true,
    },
    plugins: [
      Alignment,
      Autoformat,
      AutoImage,
      Autosave,
      BlockQuote,
      Bold,
      Emoji,
      Essentials,
      GeneralHtmlSupport,
      Heading,
      HorizontalLine,
      ImageBlock,
      ImageCaption,
      ImageInline,
      ImageInsert,
      ImageInsertViaUrl,
      ImageResize,
      ImageStyle,
      ImageTextAlternative,
      ImageToolbar,
      ImageUpload,
      Indent,
      IndentBlock,
      Italic,
      Link,
      LinkImage,
      List,
      ListProperties,
      MediaEmbed,
      Mention,
      Paragraph,
      PasteFromOffice,
      SimpleUploadAdapter,
      SourceEditing,
      Strikethrough,
      Table,
      TableCaption,
      TableCellProperties,
      TableColumnResize,
      TableProperties,
      TableToolbar,
      TextTransformation,
      TodoList,
      Underline,
    ],
    heading: {
      options: [
        {
          model: 'paragraph',
          title: 'Paragraph',
          class: 'ck-heading_paragraph',
        },
        {
          model: 'heading1',
          view: 'h1',
          title: 'Heading 1',
          class: 'ck-heading_heading1',
        },
        {
          model: 'heading2',
          view: 'h2',
          title: 'Heading 2',
          class: 'ck-heading_heading2',
        },
        {
          model: 'heading3',
          view: 'h3',
          title: 'Heading 3',
          class: 'ck-heading_heading3',
        },
        {
          model: 'heading4',
          view: 'h4',
          title: 'Heading 4',
          class: 'ck-heading_heading4',
        },
        {
          model: 'heading5',
          view: 'h5',
          title: 'Heading 5',
          class: 'ck-heading_heading5',
        },
        {
          model: 'heading6',
          view: 'h6',
          title: 'Heading 6',
          class: 'ck-heading_heading6',
        },
      ],
    },
    htmlSupport: {
      allow: [
        {
          name: /^.*$/,
          styles: true,
          attributes: true,
          classes: true,
        },
      ],
    },
    image: {
      toolbar: [
        'toggleImageCaption',
        'imageTextAlternative',
        '|',
        'imageStyle:inline',
        'imageStyle:wrapText',
        'imageStyle:breakText',
        '|',
        'resizeImage',
      ],
    },
    initialData: '<h2>Congratulations on setting up CKEditor 5!</h2>',
    language: 'zh-cn',
    licenseKey: LICENSE_KEY,
    link: {
      addTargetToExternalLinks: true,
      defaultProtocol: 'https://',
      decorators: {
        toggleDownloadable: {
          mode: 'manual',
          label: 'Downloadable',
          attributes: {
            download: 'file',
          },
        },
      },
    },
    list: {
      properties: {
        styles: true,
        startIndex: true,
        reversed: true,
      },
    },
    mention: {
      feeds: [
        {
          marker: '@',
          feed: [
            /* See: https://ckeditor.com/docs/ckeditor5/latest/features/mentions.html */
          ],
        },
      ],
    },
    placeholder: 'Type or paste your content here!',
    table: {
      contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties'],
    },
    translations: [translations],
    simpleUpload: {
      // The URL that the images are uploaded to.
      uploadUrl: import.meta.env.VITE_CKEDITOR_UPLOAD_IMG,

      // Enable the XMLHttpRequest.withCredentials property.
      withCredentials: true,

      // Headers sent along with the XMLHttpRequest to the upload server.
      headers: {
        oleduauthorization: tokenInfo.value,
      },
    },
  };
});

// 定义插件
const CKEditorConfigPlugin: Plugin = {
  install(app) {
    // 注入全局配置
    app.provide('ckeditorConfig', config);
    app.provide('ckeditorEditor', editor);
  },
};

export default CKEditorConfigPlugin;
