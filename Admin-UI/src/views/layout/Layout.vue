<template>
	<div class="app-wrapper" :class="{hideSidebar:false}">
    <div class="teste">
      <!--@mouseover="toggleSideBar" @mouseout="toggleSideBar"-->
    <navbar></navbar>
    <sidebar class="sidebar-container" v-if="sidebar.opened"></sidebar>
    </div>
		<div class="main-container">
			<app-main></app-main>
		</div>
	</div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from 'views/layout'

export default {
  name: 'layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	@import "src/styles/mixin.scss";
	.app-wrapper {
		@include clearfix;
		position: relative;
		height: 100%;
		width: 100%;
		&.hideSidebar {
			.sidebar-container{
				width:36px;
				overflow: inherit;
			}
			.main-container {
				margin-left: 0px;
			}
		}
		.sidebar-container {
			transition: width 0.28s ease-out;
      background-color: '#304156';
			width: 180px;
			height: 94.5%;
			position: fixed;
			/*top: 0;*/
			bottom: 0;
			left: 0;
			z-index: 1001;
			overflow-y: auto;
 			&::-webkit-scrollbar {display:none}
		}
		.main-container {
			min-height: 100%;
			transition: margin-left 0.28s ease-out;
			margin-left: 0px;
		}
	}
</style>
