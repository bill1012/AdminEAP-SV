<template>
  <el-menu mode="vertical"
           unique-opened :default-active="$route.path"
           :collapse="isCollapse"
           height= "100%"
           class="menu_main">
    <sidebar-item :routes='permission_routers'>
    </sidebar-item>
  </el-menu>
</template>


<script>
  import { mapGetters } from 'vuex'
  import SidebarItem from './SidebarItem'
  export default {
    components: { SidebarItem },
    computed: {
      ...mapGetters([
        'permission_routers',
        'sidebar'
      ]),
      isCollapse() {
        return !this.sidebar.opened
      }
    }
  }
</script>
<style>

  .menu_main {
    background-color: "#304156";
    text-color: "#bfcbd9";
    active-text-color: "#f4f4f5";
  }


</style>
