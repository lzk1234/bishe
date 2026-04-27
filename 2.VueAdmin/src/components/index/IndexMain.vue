<template>
	<el-container class="index-container">
		<index-header></index-header>
		<el-container class="main-body">
			<el-aside class="sidebar-wrapper" width="240px">
				<index-aside></index-aside>
			</el-aside>
			<el-main class="main-content">
				<router-view class="router-view"></router-view>
			</el-main>
		</el-container>
	</el-container>
</template>

<script>
	import IndexHeader from '@/components/index/IndexHeader'
	import IndexAside from '@/components/index/IndexAsideStatic'
	export default {
		components: {
			IndexHeader,
			IndexAside
		}
	}
</script>

<style lang="scss" scoped>
	.index-container {
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		overflow: visible;
		background: #f0f4f2;

		.main-body {
			flex: 1 0 auto;
			display: flex;
			align-items: flex-start;
			overflow: visible;
			min-height: calc(100vh - 64px);
		}

		.sidebar-wrapper {
			height: calc(100vh - 64px);
			position: sticky;
			top: 64px;
			background: #f8faf9;
			border-right: 1px solid #e1e8e5;
			overflow-y: auto;
			overflow-x: hidden;
		}

		.main-content {
			flex: 1;
			padding: 24px;
			position: relative;
			overflow: visible;
			min-width: 0;
		}

		.router-view {
			background: #ffffff;
			border-radius: 12px;
			padding: 20px;
			box-shadow: 0 4px 16px rgba(0,0,0,0.05);
		}
	}
</style>
