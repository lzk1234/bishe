<template>
	<div class="menu-preview">
		<el-scrollbar wrap-class="scrollbar-wrapper">
			<el-menu :default-openeds="[]" :unique-opened="true" class="aside-menu" default-active="0">
				<el-submenu index="0" @click.native="menuHandler('')" class="menu-home">
					<template slot="title">
						<i class="el-icon-s-home"></i>
						<span>系统首页</span>
					</template>
				</el-submenu>
				
				<el-submenu index="1">
					<template slot="title">
						<i class="el-icon-user-solid"></i>
						<span>个人中心</span>
					</template>
					<el-menu-item index="1-1" @click="menuHandler('updatePassword')">修改密码</el-menu-item>
					<el-menu-item index="1-2" @click="menuHandler('center')">个人信息</el-menu-item>
				</el-submenu>
				<el-submenu v-for=" (menu,index) in menuList.backMenu" :key="menu.menu" :index="index+2+''">
					<template slot="title">
						<i class="el-icon-menu" :class="icons[index]"></i>
						<span>{{ menu.menu }}</span>
					</template>
					<el-menu-item v-for=" (child,sort) in menu.child" :key="sort" :index="(index+2)+'-'+sort" @click="menuHandler(child.tableName)">{{ child.menu }}</el-menu-item>
				</el-submenu>
			</el-menu>
		</el-scrollbar>
	</div>
</template>

<script>
import menu from '@/utils/menu'
export default {
	data() {
		return {
			menuList: [],
			dynamicMenuRoutes: [],
			role: '',
			icons: [
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-fold',
				'el-icon-s-unfold',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-custom',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check',
				'el-icon-s-grid',
				'el-icon-menu',
				'el-icon-chat-dot-square',
				'el-icon-message',
				'el-icon-postcard',
				'el-icon-position',
				'el-icon-microphone',
				'el-icon-close-notification',
				'el-icon-bangzhu',
				'el-icon-time',
				'el-icon-odometer',
				'el-icon-crop',
				'el-icon-aim',
				'el-icon-switch-button',
				'el-icon-full-screen',
				'el-icon-copy-document',
				'el-icon-mic',
				'el-icon-stopwatch',
			],
			menulistBorderBottom: {},
		}
	},
	mounted() {
		let menus = menu.list()
		// Ensure menus is always an array
		if(!menus) {
			// Try to get from storage
			const storedMenus = this.$storage.get("menus")
			if(storedMenus) {
				menus = storedMenus
			}
		}
		
		// If still no menus, fetch from API
		if(!menus) {
			let params = {
				page: 1,
				limit: 1,
				sort: 'id',
			}
			
			this.$http({
				url: "menu/list",
				method: "get",
				params: params
			}).then(({
				data
			}) => {
				if (data && data.code === 0) {
					const menuData = JSON.parse(data.data.list[0].menujson);
					this.$storage.set("menus", menuData);
					this.processMenuData(menuData);
				}
			})
		} else {
			this.processMenuData(menus);
		}
	},
	methods: {
		processMenuData(menus) {
			// Ensure menus is an array
			if(!Array.isArray(menus)) {
				menus = [menus];
			}
			
			this.role = this.$storage.get('role')
			
			// Find matching role menu
			let found = false;
			for(let i=0; i<menus.length; i++) {
				if(menus[i].roleName == this.role) {
					this.menuList = menus[i];
					found = true;
					break;
				}
			}
			
			// If no matching role found, use first menu item or empty object
			if(!found) {
				this.menuList = menus[0] || { backMenu: [], frontMenu: [] };
			}
		},
		menuHandler(name) {
			let router = this.$router
			name = '/'+name
			router.push(name)
		},
	},
	created(){
		this.icons.sort(()=>{
			return (0.5-Math.random())
		})
	}
}
</script>
<style lang="scss" scoped>
	.menu-preview {
		height: 100%;
		background: #f8faf9;

		.el-scrollbar {
			height: 100%;

			& ::v-deep .scrollbar-wrapper {
				overflow-x: hidden;
			}
		}

		.aside-menu {
			border-right: none;
			background: transparent;
			padding: 12px;

			// 首页菜单项隐藏箭头
			.menu-home ::v-deep .el-submenu__icon-arrow {
				display: none;
			}

			::v-deep .el-menu-item, ::v-deep .el-submenu__title {
				height: 50px;
				line-height: 50px;
				border-radius: 8px;
				margin-bottom: 4px;
				color: #4a5d55;
				transition: all 0.3s;
				padding: 0 10px;

				i {
					color: #7a8c85;
					margin-right: 10px;
					font-size: 18px;
				}

				&:hover {
					background-color: #ecf2f0 !important;
					color: #2d5a27;

					i {
						color: #2d5a27;
					}
				}
			}

			::v-deep .el-menu-item.is-active {
				background-color: #2d5a27 !important;
				color: #ffffff !important;
				box-shadow: 0 4px 12px rgba(45, 90, 39, 0.2);

				i {
					color: #ffffff;
				}
			}

			// 子菜单展开项
			::v-deep .el-submenu .el-menu .el-menu-item {
				padding: 0 40px;
				color: #666;
				background: transparent;
				border: none;
				line-height: 50px;
				height: 50px;
				min-width: auto;

				&:hover {
					background-color: #ecf2f0 !important;
					color: #2d5a27;
				}

				&.is-active {
					background-color: #2d5a27 !important;
					color: #ffffff !important;
				}
			}
		}
	}
</style>
