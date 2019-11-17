<template>
  <el-row :gutter="15">
    <el-col :span="6">
      <div style="text-align:left;margin-bottom:10px;">
        <el-button v-if="roleManager_menu" @click="cancel">取消</el-button>
        <el-button type="primary" v-if="roleManager_menu" @click="update">保存</el-button>
      </div>
      <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
      <!--check-strictly 各节点分离-->
      <el-tree class="filter-tree" checkStrictly :node-key="getTreeNodeKey" :data="treeData" show-checkbox
               node-key="id" highlight-current :props="defaultProps" :filter-node-method="filterNode" ref="menuTree"
               @node-click="getNodeData" default-expand-all style="margin-top:10px;">
      </el-tree>
    </el-col>
    <el-col :span="18">
      <div style="text-align:left;margin-bottom:10px;"><strong>{{menuName}}</strong></div>
      <el-table ref="elementTable" :data="list" border fit highlight-current-row @select="handleSelectionChange"
                @select-all="handleSelectionAll"
                style="width: 100%">
        <el-table-column v-if="roleManager_element" type="selection" width="61"></el-table-column>
        <el-table-column width="250px" align="center" label="资源编码">
          <template slot-scope="scope">
            <span>{{scope.row.code}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="资源类型">
          <template slot-scope="scope">
            <span>{{scope.row.type}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="资源名称">
          <template slot-scope="scope">
            <span> {{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="资源地址">
          <template slot-scope="scope">
            <span>{{scope.row.uri}}</span>
          </template>
        </el-table-column>
        <el-table-column width="150px" align="center" label="请求类型">
          <template slot-scope="scope">
            <span>{{scope.row.method}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="描述">
          <template slot-scope="scope">
            <span>{{scope.row.description}}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>

</template>
<script>
  import {fetchTree} from 'api/admin/menu/index';
  import {list} from 'api/admin/element/index';
  import {
    deleteElementResource,
    addElementResource,
    modifyElementResource,
    getElementResource,
    modifyMenuResource,
    getMenuResource
  } from 'api/admin/role/resource';
  import {mapGetters} from 'vuex';

  export default {
    name: 'menu1',
    props: {
      roleId: {
        default: '-1'
      }
    },
    data() {
      return {
        filterText: '',
        list: null,
        total: null,
        listQuery: {
          name: undefined
        },
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        roleManager_menu: false,
        roleManager_element: false,
        menuName: '按钮资源'
      }
    },
    watch: {
      filterText(val) {
        this.$refs.menuTree.filter(val);
      }
    },
    created() {
      this.getList();
      this.roleManager_menu = true; // this.elements['roleManager:menu'];
      this.roleManager_element = true;// this.elements['roleManager:element'];
    },
    computed: {
      ...mapGetters([
        'elements'
      ])
    },
    methods: {
      getList() {
        fetchTree(this.listQuery).then(data => {
          this.treeData = data;
          this.initResources();
        });
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      getNodeData(data) {
        this.listQuery.menuId = data.id;
        this.menuName = '【' + data.name + '】关联资源';
        list(this.listQuery).then(response => {
          this.list = response.data.rows;
          getElementResource(this.roleId).then(data => {
            const obj = {};
            for (let i = 0; i < this.list.length; i++) {
              obj[this.list[i].id] = this.list[i];
            }
            const toggle = {};
            for (let i = 0; i < data.data.length; i++) {
              const id = data.data[i]
              if (obj[id] !== undefined && toggle[id] === undefined) {
                this.$refs.elementTable.toggleRowSelection(obj[data.data[i]]);
                toggle[id] = true;
              }
            }
          });
        });
        this.currentId = data.id;
      },
      getTreeNodeKey(node) {
        return node.id;
      },
      handleSelectionChange(val, row) {
        let flag = true;
        for (let i = 0; i < val.length; i++) {
          if (val[i].id === row.id) {
            addElementResource(this.roleId, {
              // menuId: this.currentId,
              elementId: row.id
            });
            flag = false;
            break;
          }
        }
        if (flag) {
          deleteElementResource(this.roleId, {
            // menuId: this.currentId,
            elementId: row.id
          });
        }
      },
      handleSelectionAll(val) {
        var ids = val.map(item => item.id).join();
        modifyElementResource(this.roleId, {
          menuId: this.currentId,
          elementIds: ids
        })
      },
      cancel() {
        this.$emit('closeDialog');
      },
      update() {
        this.$emit('closeDialog');
        const nodes = this.$refs.menuTree.getCheckedNodes();
        const ids = [];
        for (let i = 0; i < nodes.length; i++) {
          ids.push(nodes[i].id);
        }
        modifyMenuResource(this.roleId, {
          menuIds: ids.join()
        }).then(() => {
          this.$notify({
            title: '成功',
            message: '保存成功',
            type: 'success',
            duration: 2000
          });
        });
      },
      initResources() {
        getMenuResource(this.roleId).then(response => {
          this.$refs.menuTree.setCheckedKeys(response.data);
        });
      }
    }
  }
</script>
