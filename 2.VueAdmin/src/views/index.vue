<template>
	<el-container>
		<index-main></index-main>
	</el-container>
</template>

<script>
	import IndexMain from '@/components/index/IndexMain'
	export default {
		components: {
			IndexMain
		}
	}
</script>

<style lang="scss" scoped>
	.el-container {
		position: relative;
		width: 100%;
		min-height: 100vh;
		display: block;
		overflow: visible;
	}
</style>
