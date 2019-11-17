<template>
  <div class="app-container calendar-list-container">
    <el-row :gutter="15">
      <el-col :span="4">
        <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
        <el-tree class="filter-tree" :data="treeData" node-key="id" highlight-current :props="defaultProps"
                 :filter-node-method="filterNode" ref="roleTree" @node-click="getNodeData"
                 default-expand-all style="margin-top:10px;"></el-tree>
      </el-col>
      <el-col :span="20">
        <div style="margin-bottom: 10px">
          <el-button-group>
            <el-button type="primary" v-if="roleManager_btn_add" icon="plus" @click="handleAdd">添加</el-button>
            <el-button type="primary" v-if="roleManager_btn_edit" icon="edit" @click="handleEdit">编辑</el-button>
            <el-button type="danger" v-if="roleManager_btn_del" icon="delete" @click="handleDelete">删除</el-button>
          </el-button-group>

          <el-button type="primary" v-if="roleManager_btn_resourceManager" icon="el-icon-menu" @click="handleResource">
            权限分配
          </el-button>
          <!-- <el-button-group>
           <el-button type="primary" v-if="roleManager_btn_userManager"  @click="handlerUser">
             <icon-svg icon-class="users"></icon-svg>
             关联用户
           </el-button>
           </el-button->-->
          <el-button-group v-if="formStatus == 'update'" style="float:right">
            <el-button type="primary" v-if="roleManager_btn_edit" @click="update">更新</el-button>
            <el-button @click="onCancel">取消</el-button>
          </el-button-group>
          <el-button-group v-if="formStatus == 'create'" style="float:right">
            <el-button type="primary" v-if="roleManager_btn_add" @click="create">保存</el-button>
            <el-button @click="onCancel">取消</el-button>
          </el-button-group>
        </div>
        <el-card style="margin-bottom:10px;">
          <el-form :label-position="labelPosition" :rules="rules" label-width="140px" :inline="true" :model="form"
                   ref="form">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" :disabled="formEdit"></el-input>
            </el-form-item>
            <el-form-item label="编码" prop="code">
              <el-input v-model="form.code" :disabled="formEdit"></el-input>
            </el-form-item>
            <el-form-item label="父级节点">
              <el-input v-model="form.parentName" disabled></el-input>
            </el-form-item>
            <el-form-item label="层级编码" prop="levelcode">
              <el-input v-model="form.levelcode" :disabled="formEdit"></el-input>
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="form.description" :disabled="formEdit"></el-input>
            </el-form-item>
            <el-form-item label="是否可用">
              <el-switch active-text="可用" inactive-text="禁用" active-value="1" inactive-value="0" v-model="form.enabled"></el-switch>
            </el-form-item>
          </el-form>
        </el-card>
        <el-menu style="margin-bottom: 10px;">
          <el-menu-item index="1"><i class="el-icon-menu">&nbsp;&nbsp;{{roleName}}</i></el-menu-item>
        </el-menu>
        <el-card class="box-card">
          <user-role :roleId="currentId" :roleName="form.name" ref="userRole"></user-role>
        </el-card>
      </el-col>

      <el-dialog :title="dialogResourceName" :visible.sync="dialogResourceVisible">
        <role-resource :roleId="currentId" @closeDialog="closeResourceDialog" ref="roleResource"></role-resource>
      </el-dialog>
    </el-row>
  </div>
</template>

<script>
  import {fetchTree, getObj, addObj, delObj, putObj, checkCode, getNextCode} from 'api/admin/role/index';
  import {mapGetters} from 'vuex';

  export default {
    name: 'role',
    components: {
      'user-role': () => import('./user'),
      'role-resource': () => import('./resource')
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
        formAdd: true,
        formStatus: '',
        dialogResourceVisible: false,
        dialogResourceName: '关联资源',
        listQuery: {
          groupType: this.type,
          name: undefined
        },
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        labelPosition: 'right',
        roleManager_btn_edit: false,
        roleManager_btn_del: false,
        roleManager_btn_add: false,
        roleManager_btn_userManager: false,
        roleManager_btn_resourceManager: false,
        form: this.init(),
        currentId: '-1',
        currentName: '根节点',
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
          levelcode: [{
            required: true,
            message: '请填写层级编码',
            trigger: 'blur'
          }]
        },
        roleName: '关联用户'
      }
    },
    watch: {
      filterText(val) {
        this.$refs.roleTree.filter(val);
      }
    },
    created() {
      this.getList();
      this.roleManager_btn_edit = true; // this.elements['roleManager:btn_edit'];
      this.roleManager_btn_del = true; // this.elements['roleManager:btn_del'];
      this.roleManager_btn_add = true; // this.elements['roleManager:btn_add'];
      this.roleManager_btn_userManager = true; // this.elements['roleManager:btn_userManager'];
      // this.roleManager_btn_resourceManager = false;  this.elements['roleManager:btn_resourceManager'];
    },
    computed: {
      ...mapGetters([
        'elements'
      ])
    },
    methods: {
      init() {
        return {
          code: undefined,
          name: undefined,
          description: undefined,
          enabled: '1',
          levelcode: undefined,
          parentId: this.currentId,
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
        this.roleName = '【' + data.name + '】关联用户';
        this.dialogResourceName = '【' + data.name + '】关联资源';

        if (this.$refs.userRole) {
          this.$refs.userRole.roleId = data.id;
          this.$refs.userRole.roleManager_btn_user_add = true;
          this.$refs.userRole.getList();
        }
        this.roleManager_btn_resourceManager = true; // this.elements['roleManager:btn_resourceManager'];
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
      },
      resetForm() {
        this.form = this.init();
      },
      handleResource() {
        this.dialogResourceVisible = true;
        if (this.$refs.roleResource !== undefined) {
          this.$refs.roleResource.roleId = this.currentId;
          this.$refs.roleResource.initResources();
        }
      },
      closeResourceDialog() {
        this.dialogResourceVisible = false;
      }
    }
  }
</script>
