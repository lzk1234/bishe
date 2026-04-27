<template>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  :root {
    --primary-color: #1B3022;
    --accent-color: #A68966;
    --bg-color: #F5F5F0;
    --card-bg: #FFFFFF;
    --text-main: #2D3436;
    --text-secondary: #636E72;
    --text-light: #B2BEC3;
    --shadow-soft: 0 4px 15px rgba(27, 48, 34, 0.05);
    --shadow-hover: 0 10px 30px rgba(27, 48, 34, 0.1);
    --radius-main: 8px;
    --transition-main: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  }

  * {
	  box-sizing: border-box;
  }
  html, body{
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    font-family: "Hiragino Sans GB", "Microsoft YaHei", "Source Han Sans CN", serif;
    background-color: var(--bg-color);
    color: var(--text-main);
    -webkit-font-smoothing: antialiased;
  }
  
  /* Global Animations */
  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .fade-in-up {
    animation: fadeInUp 0.8s cubic-bezier(0.22, 1, 0.36, 1) forwards;
  }

  /* Element UI Customizations */
  .el-button--primary {
    background-color: var(--primary-color) !important;
    border-color: var(--primary-color) !important;
    color: var(--accent-color) !important;
    font-weight: 500 !important;
    letter-spacing: 1px;
    transition: var(--transition-main) !important;
  }
  .el-button--primary:hover {
    background-color: #26412e !important;
    border-color: #26412e !important;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(27, 48, 34, 0.2);
  }
  
  .el-button--success {
    background-color: var(--accent-color) !important;
    border-color: var(--accent-color) !important;
    color: #fff !important;
  }

  .el-card {
    border: 1px solid #ebebeb !important;
    border-radius: var(--radius-main) !important;
    box-shadow: none !important;
    transition: var(--transition-main) !important;
  }
  .el-card:hover {
    box-shadow: var(--shadow-hover) !important;
    border-color: transparent !important;
  }

  [v-cloak]{
    display:none
  }
  .el-tabs__item {
    font-size: 16px;
    color: var(--text-secondary);
  }
  .el-tabs__item.is-active {
    color: var(--primary-color);
    font-weight: 600;
  }
  .el-loading-spinner .el-loading-text {
    color: var(--primary-color);
  }
  .el-loading-spinner .path {
    stroke: var(--primary-color);
  }
  .el-breadcrumb {
    line-height: 60px;
    font-size: 14px;
  }
  .el-breadcrumb__inner.is-link:hover {
    color: var(--primary-color);
  }

  .ql-editor {
    /*height: 500px;*/
  }
  .ql-snow .ql-tooltip[data-mode="link"]::before {
    content: "请输入链接地址:";
  }
  .ql-snow .ql-tooltip.ql-editing a.ql-action::after {
    border-right: 0px;
    content: "保存";
    padding-right: 0px;
  }
  .ql-snow .ql-tooltip[data-mode="video"]::before {
    content: "请输入视频地址:";
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item::before {
    content: "14px";
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
    content: "10px";
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
    content: "18px";
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
    content: "32px";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item::before {
    content: "文本";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
    content: "标题1";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
    content: "标题2";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
    content: "标题3";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
    content: "标题4";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
    content: "标题5";
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
    content: "标题6";
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item::before {
    content: "标准字体";
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
    content: "衬线字体";
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
    content: "等宽字体";
  }
</style>
