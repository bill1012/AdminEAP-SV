<template>
  <div class="dashboard-container">
    <component :is="currentRole"></component>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin/index'
import editorDashboard from './editor/index'
import projectItems from '../item/project/index'
export default {
  name: 'dashboard',
  components: { adminDashboard, editorDashboard, projectItems},
  data() {
    return {
      currentRole: projectItems // 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (this.roles.indexOf('admin') >= 0) {
      return
    }
    // this.currentRole = 'editorDashboard'
    this.currentRole = 'projectItems'
  }
}
</script>
