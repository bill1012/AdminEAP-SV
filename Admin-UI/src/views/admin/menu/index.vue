<template>
  <div class="app-container calendar-list-container">
    <el-row :gutter="15">
      <el-col :span="4">
        <el-input
          placeholder="输入关键字进行过滤"
          v-model="filterText">
        </el-input>
        <el-tree
          class="filter-tree"
          style="margin-top:10px;"
          :data="treeData"
          node-key="id"
          highlight-current
          :props="defaultProps"
          :filter-node-method="filterNode"
          ref="menuTree"
          @node-click="getNodeData"
          default-expand-all>
        </el-tree>
      </el-col>
      <el-col :span="20">
        <div class="filter-container">
          <el-button-group>
            <el-button type="primary" v-if="menuManager_btn_add" icon="plus" @click="handleAdd">添加</el-button>
            <el-button type="primary" v-if="menuManager_btn_edit" icon="edit" @click="handleEdit">编辑</el-button>
            <el-button type="danger" v-if="menuManager_btn_del" icon="delete" @click="handleDelete">删除</el-button>
          </el-button-group>

          <el-button-group v-if="formStatus == 'update'" style="float: right">
            <el-button type="primary" @click="update">更 新</el-button>
            <el-button @click="onCancel">取 消</el-button>
          </el-button-group>

          <el-button-group v-if="formStatus == 'create'" style="float:right">
            <el-button type="primary" @click="create">保 存</el-button>
            <el-button @click="onCancel">取 消</el-button>
          </el-button-group>
        </div>
        <el-card class="box-card" style="margin-bottom: 10px;">
          <el-form :label-position="labelPosition" label-width="140px" :rules="rules" :inline="true" :model="form"
                   ref="form">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" :disabled="formEdit" placeholder="请输入标题"></el-input>
            </el-form-item>
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" :disabled="formEdit" placeholder="请输入编码"></el-input>
            </el-form-item>
            <el-form-item label="父级节点" prop="parentName">
              <el-input v-model="form.parentName" :disabled="formEdit" placeholder="请输入父级节点" disabled></el-input>
            </el-form-item>
            <el-form-item label="层级编码" prop="levelcode">
              <el-input v-model="form.levelcode" :disabled="formEdit" placeholder="请输入层级编码"></el-input>
            </el-form-item>
            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" :disabled="formEdit" style="width: 320px;" placeholder="请输入图标"></el-input>
              <icon-svg v-if="form.icon!=null" :iconClass="form.icon" color="grey"></icon-svg>
            </el-form-item>
            <el-form-item label="资源路径" prop="href">
              <el-input v-model="form.href" :disabled="formEdit" placeholder="请输入资源路径"></el-input>
            </el-form-item>
            <el-form-item label="类型" prop="type">
              <el-select class="filter-item" v-model="form.type" :disabled="formEdit" style="width: 340px;"
                         placeholder="请输入资源请求类型">
                <el-option v-for="item in  typeOptions" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="前端组件" prop="component">
              <el-input v-model="form.component" :disabled="formEdit" placeholder="请输入描述"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" :disabled="formEdit" placeholder="请输入描述"></el-input>
            </el-form-item>
            <el-form-item label="是否启用" prop="enabled">
              <el-switch active-text="启用" inactive-text="禁用" active-value="1" inactive-value="0" v-model="form.enabled"></el-switch>
            </el-form-item>

          </el-form>
        </el-card>

        <el-menu style="margin-bottom: 10px;">
          <el-menu-item index="1"><i class="el-icon-menu">&nbsp;&nbsp;{{menuName}}</i></el-menu-item>
        </el-menu>
        <el-card class="box-card">
          <menu-element :menuId='currentId' ref="menuElement" style="padding: 0px;"></menu-element>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {fetchTree, getObj, addObj, delObj, putObj, checkCode, getNextCode} from 'api/admin/menu/index';
  import {mapGetters} from 'vuex';
  import MenuElement from './element'

  export default {
    name: 'menu',
    components: {
      MenuElement
    },
    data() {
      const validateCode = (rule, value, callback) => {
        if (!value) {
          callback(new Error('请填写编码'))
        }
        checkCode(value, this.form.id).then(result => {
          if (!result) {
            callback(new Error('该编码已存在'))
          } else {
            callback()
          }
        });
      };
      return {
        filterText: '',
        list: null,
        total: null,
        formEdit: true,
        formStatus: '',
        showElement: false,
        typeOptions: ['menu', 'link'],
        listQuery: {
          name: undefined
        },
        rules: {
          name: [{
            required: true,
            message: '请填写名称',
            trigger: 'blur'
          }],
          code: [{
            required: true,
            trigger: 'blur',
            validator: validateCode
          }],
          type: [{
            required: true,
            trigger: 'blur',
            message: '请填写类型'
          }],
          levelcode: [{
            required: true,
            message: '请填写层级编码',
            trigger: 'blur'
          }]
        },
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        labelPosition: 'right',
        form: this.init(),
        currentId: '-1',
        currentName: '根节点',
        menuName: '按钮或资源',
        menuManager_btn_add: false,
        menuManager_btn_edit: false,
        menuManager_btn_del: false
      }
    },
    watch: {
      filterText(val) {
        this.$refs.menuTree.filter(val);
      }
    },
    created() {
      this.getList();
      this.menuManager_btn_add = true;  // this.elements['menuManager:btn_add'];
      this.menuManager_btn_del = true;  // this.elements['menuManager:btn_del'];
      this.menuManager_btn_edit = true; // this.elements['menuManager:btn_edit'];
    },
    computed: {
      ...mapGetters([
        'elements'
      ])/*,
      iconCls: function() {
        return 'el-icon-' + this.form.icon;
      }*/
    },
    methods: {
      init() {
        return {
          code: undefined,
          name: undefined,
          parentId: this.currentId,
          href: undefined,
          icon: undefined,
          component: undefined,
          levelcode: undefined,
          description: undefined,
          enabled: '1',
          type: undefined,
          parentName: this.currentName
        }
      },
      getList() {
        fetchTree(this.listQuery).then(data => {
          this.treeData = data;
        });
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      getNodeData(data) {
        if (!this.formEdit) {
          this.formStatus = 'update';
        }
        getObj(data.id).then(response => {
          this.form = response.data;
        });
        this.currentId = data.id;
        this.currentName = data.name;
        this.showElement = true;
        this.menuName = '【' + data.name + '】按钮或资源';
        if (this.$refs.menuElement) {
          this.$refs.menuElement.menuId = data.id;
          this.$refs.menuElement.initObj(data);
          this.$refs.menuElement.getList();
        }
      },
      handleEdit() {
        if (this.form.id) {
          this.formEdit = false;
          this.formStatus = 'update';
        } else {
          this.$notify({
            title: '提示',
            message: '请选择要编辑的节点',
            type: 'info',
            duration: 2000
          });
        }
      },
      handleAdd() {
        this.resetForm();
        this.formEdit = false;
        this.formStatus = 'create';
        getNextCode(this.form.parentId).then(nextCode => {
          this.form.levelcode = nextCode;
        });
      },

      handleDelete() {
        if (this.form.id) {
          this.$confirm('此操作将永久删除, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            delObj(this.currentId).then(() => {
              this.getList();
              this.resetForm();
              this.onCancel();
              this.$notify({
                title: '成功',
                message: '删除成功',
                type: 'success',
                duration: 2000
              });
            });
          });
        } else {
          this.$notify({
            title: '提示',
            message: '请选择要删除的节点',
            type: 'info',
            duration: 2000
          });
        }
      },
      update() {
        const set = this.$refs
        set['form'].validate(valid => {
          if (valid) {
            putObj(this.form.id, this.form).then(() => {
              this.getList();
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              });
            });
          } else {
            return false;
          }
        })
      },
      create() {
        const set = this.$refs
        set['form'].validate(valid => {
          if (valid) {
            addObj(this.form).then(() => {
              this.getList();
              this.$notify({
                title: '成功',
                message: '创建成功',
                type: 'success',
                duration: 2000
              });
            });
          } else {
            return false;
          }
        })
      },
      onCancel() {
        this.formEdit = true;
        this.formStatus = '';
        this.$refs['form'].resetFields();
      },
      resetForm() {
        this.$refs['form'].resetFields();
        this.form = this.init();
      }
    }
  }
</script>

