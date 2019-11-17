<template>
  <div class="mixin-components-container">

    <el-row :gutter="20" style="margin-top:50px;">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Share</span>
          </div>
          <div class="component-item" style="height:420px;">
            <dropdown-menu :items="articleList" style="margin:0 auto;" title="系列文章" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>Share</span>
          </div>
          <div class="component-item" style="height:420px;">
            <dropdown-menu :items="articleList" style="margin:0 auto;" title="系列文章" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import PanThumb from '@/components/PanThumb'
  import MdInput from '@/components/MDinput'
  import Mallki from '@/components/TextHoverEffect/Mallki'
  import DropdownMenu from '@/components/Share/DropdownMenu'
  import waves from '@/directive/waves/index.js' // 水波纹指令

  export default {
    name: 'ComponentMixinDemo',
    components: {
      PanThumb,
      MdInput,
      Mallki,
      DropdownMenu
    },
    directives: {
      waves
    },
    data() {
      const validate = (rule, value, callback) => {
        if (value.length !== 6) {
          callback(new Error('请输入六个字符'))
        } else {
          callback()
        }
      }
      return {
        demo: {
          title: ''
        },
        demoRules: {
          title: [{ required: true, trigger: 'change', validator: validate }]
        },
        articleList: [
          { title: '基础篇', href: 'https://juejin.im/post/59097cd7a22b9d0065fb61d2' },
          { title: '登录权限篇', href: 'https://juejin.im/post/591aa14f570c35006961acac' },
          { title: '实战篇', href: 'https://juejin.im/post/593121aa0ce4630057f70d35' },
          { title: 'vue-admin-template 篇', href: 'https://juejin.im/post/595b4d776fb9a06bbe7dba56' },
          { title: 'v4.0 篇', href: 'https://juejin.im/post/5c92ff94f265da6128275a85' },
          { title: '优雅的使用 icon', href: 'https://juejin.im/post/59bb864b5188257e7a427c09' }
        ]
      }
    }
  }
</script>

<style scoped>
  .mixin-components-container {
    background-color: #f0f2f5;
    padding: 30px;
    min-height: calc(100vh - 84px);
  }
  .component-item{
    min-height: 100px;
  }
</style>
