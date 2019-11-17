<template>
  <div id="org" class="app-container calendar-list-container">
    <el-input placeholder="输入关键字进行过滤" v-model="filterText">
    </el-input>
    <el-tree class="filter-tree on-border" :data="data2" :props="defaultProps" :show-checkbox="true"  :default-expand-all="true"  :filter-node-method="filterNode" :default-expanded-keys="[0,1]" ref="tree2" @node-click="clickTree" @node-expand="expandTree">
    </el-tree>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel()">取 消</el-button>
      <el-button type="primary" @click="checkOrgNodes()" >确 定</el-button>
    </div>
  </div>              
</template>
<script>
  import { orgTree } from 'api/admin/org/index'
  export default {
    watch: {
      filterText(val) {
        this.$refs.tree2.filter(val);
      }
    },
    created() {
      // 初始化左侧树
      this.getOrgTree()
    },
    methods: {
      getOrgTree() {
        this.listLoading = true
        var that = this
        orgTree(this.treeForm).then(response => {
          console.log(response.data)
          that.$set(that, 'data2', JSON.parse(response.data))
           // 获取左侧树第一个节点，初始化页面
          // this.getObj(JSON.parse(response.data)[0].id)
          // this.dataTOForm(JSON.parse(response.data)[0])
        })
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1 || data.lspell.indexOf(value) !== -1 || data.uspell.indexOf(value) !== -1 || data.initials.indexOf(value) !== -1;
      },
      clickTree(data) {
        console.log(data)
        // this.dataTOForm(data)
        // this.getObj(data.id)
      },
      expandTree(data) {
        console.log(data)
      },
      checkOrgNodes() {
        this.$emit('org_tree', this.$refs.tree2.getCheckedNodes());
      },
      cancel() {
        this.$emit('org_tree_cancel', 'cancel');
      }
    },
    data() {
      return {
        filterText: '',
        state: 'see',
        data2: [{
          id: 1,
          label: '一级 1'
        }, {
          id: 2,
          label: '一级 2',
          children: [{
            id: 5,
            label: '二级 2-1'
          }, {
            id: 6,
            label: '二级 2-2'
          }]
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        treeForm: {
          parentid: 0
        }
      };
    }
  }
</script>
<style>
.right-layout-from{
  margin-left: 20px;
}
.top-button{
  margin-bottom: 20px;
}
.tree-right-from{
  width: 70%;
  margin: 3%;
}
.grid-content{
    border-radius: 4px;
    min-height: 36px;
}
.on-border{
  border: none
}
</style>
 