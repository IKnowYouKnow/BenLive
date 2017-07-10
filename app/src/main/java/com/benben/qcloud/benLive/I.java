package com.benben.qcloud.benLive;

public interface I {

    public static interface User {
		String TABLE_NAME							=		"t_user";
		String USER_NAME 							= 		"user_name";					//用户账号
		String PASSWORD 							= 		"password_data";				//用户密码
		String NICK 								= 		"user_nickname";					//用户昵称
		String PHONE_NUM 							= 		"phone_data";					//用户昵称
		String INVITE_CODE 							= 		"invite_code";					//用户昵称
	}
	
	public static interface Contact {
		String TABLE_NAME 							= 		"t_superwechat_contact";
		String CONTACT_ID 							= 		"m_contact_id";					//主键
		String USER_NAME 							= 		"m_contact_user_name";			//用户账号
		String CU_NAME 								= 		"m_contact_cname";				//好友账号
	}
	
	public static interface Group {
		String TABLE_NAME 							= 		"t_superwechat_group";
		String GROUP_ID 							= 		"m_group_id";					//主键
		String HX_ID 								= 		"m_group_hxid";					//环信群组id
		String NAME 								= 		"m_group_name";					//群组名称
		String DESCRIPTION 							= 		"m_group_description";			//群组简介
		String OWNER 								= 		"m_group_owner";				//群组所有者－用户账号
		String MODIFIED_TIME 						= 		"m_group_last_modified_time";	//最后修改时间
		String MAX_USERS 							= 		"m_group_max_users";			//最大人数
		String AFFILIATIONS_COUNT 					= 		"m_group_affiliations_count";	//群组人数
		String IS_PUBLIC 							= 		"m_group_is_public";			//群组是否公开
		String ALLOW_INVITES 						= 		"m_group_allow_invites";		//是否可以邀请
	}
	
	public static interface Member {
		String TABLE_NAME 							= 		"t_superwechat_member";
		String MEMBER_ID 							= 		"m_member_id";					//主键
		String USER_NAME 							= 		"m_member_user_name";			//用户账号
		String GROUP_ID 							= 		"m_member_group_id";			//群组id
		String GROUP_HX_ID 							= 		"m_member_group_hxid";			//群组环信id
		String PERMISSION 							= 		"m_member_permission";			//用户对群组的权限\n0:普通用户\n1:群组所有者
	}
	
	public static interface Avatar {
		String TABLE_NAME 							= 		"t_superwechat_avatar";
		String AVATAR_ID 							= 		"m_avatar_id";					//主键
		String USER_NAME 							= 		"m_avatar_user_name";			//用户账号或者群组账号
		String AVATAR_SUFFIX                        =       "m_avatar_suffix";              //头像后缀名
		String AVATAR_PATH 							= 		"m_avatar_path";				//保存路径
		String AVATAR_TYPE 							= 		"m_avatar_type";				//头像类型：\n0:用户头像\n1:群组头像
		String UPDATE_TIME 							= 		"m_avatar_last_update_time";	//最后更新时间
	}
	
	public static interface Location {
		String TABLE_NAME 							= 		"t_superwechat_location";
		String LOCATION_ID 							= 		"m_location_id";				//主键
		String USER_NAME 							= 		"m_location_user_name";			//用户账号
		String LATITUDE 							= 		"m_location_latitude";			//纬度
		String LONGITUDE 							= 		"m_location_longitude";			//经度
		String IS_SEARCHED 							= 		"m_location_is_searched";		//是否可以被搜索到
		String UPDATE_TIME 							= 		"m_location_last_update_time";	//最后更新时间
	}

	String UTF_8 									= 		"utf-8";

	String SERVER_ROOT = "http://192.168.0.129/app.mmhzyf.com/index.php/Info/";

	/** 上传头像图片的类型：user_avatar或group_icon */
	String AVATAR_TYPE 								= 		"avatarType";
	/** 用户的账号或群组的环信id */
	String NAME_OR_HXID                             =       "name_or_hxid";
	/** 客户端发送的获取服务端状态的请求 */
	String REQUEST_SERVERSTATUS 					= 		"getServerStatus";
	/** 客户端发送的新用户注册的请求 */
	String REQUEST_REGISTER		 					= 		"register";
	/** 客户端发送的取消注册的请求 */
	String REQUEST_UNREGISTER 						= 		"unregister";
	/** 客户端发送的用户登录请求 */
	String REQUEST_LOGIN 							= 		"login";
	/** 客户端发送的下载用户头像请求 */
	String REQUEST_DOWNLOAD_AVATAR	 				= 		"downloadAvatar";
	/** 客户端发送的上传/更新用户头像的请求 */
	String REQUEST_UPDATE_AVATAR 					= 		"updateAvatar";
	/** 客户端发送的更新用户昵称的请求 */
	String REQUEST_UPDATE_USER_NICK 				= 		"updateNick";
	/** 客户端发送的更新用户密码的请求 */
	String REQUEST_UPDATE_USER_PASSWORD 			= 		"updatePassword";
	/** 客户端发送的下载用户的好友列表的全部数据的请求 */
	String REQUEST_DOWNLOAD_CONTACT_ALL_LIST 		= 		"downloadContactAllList";
	/** 客户端发送的分页下载用户的好友列表的全部数据的请求 */
	String REQUEST_DOWNLOAD_CONTACT_PAGE_LIST 		= 		"downloadContactPageList";
	/** 客户端发送的添加好友的请求 */
	String REQUEST_ADD_CONTACT 						= 		"addContact";
	/** 客户端发送的删除好友的请求 */
	String REQUEST_DELETE_CONTACT 					= 		"deleteContact";
	/** 客户端发送的根据用户名查找用户信息的请求 */
	String REQUEST_FIND_USER 						= 		"findUserByUserName";
	/** 客户端发送的根据用户名或昵称模糊分页查找用户数据的请求 */
	String REQUEST_FIND_USERS_FOR_SEARCH			= 		"findUsersForSearch";
	/** 客户端发送的创建群组的请求 */
	String REQUEST_CREATE_GROUP			 			= 		"createGroup";
	/** 客户端发送的更新群组名称的请求 */
	String REQUEST_UPDATE_GROUP_NAME 				= 		"updateGroupName";
	/** 客户端发送的更新群组名称的请求 */
	String REQUEST_UPDATE_GROUP_NAME_BY_HXID 		= 		"updateGroupNameByHxId";
	/** 客户端发送的添加群成员的请求 */
	String REQUEST_ADD_GROUP_MEMBER 				= 		"addGroupMember";
	/** 客户端发送的批量添加群成员的请求 */
	String REQUEST_ADD_GROUP_MEMBERS		 		= 		"addGroupMembers";
	/** 客户端发送的根据群组ID下载全部群成员信息的请求 */
	String REQUEST_DOWNLOAD_GROUP_MEMBERS 			= 		"downloadGroupMembersByGroupId";
	/** 客户端发送的根据群组ID分页下载群成员信息的请求 */
	String REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_LIMIT 	= 		"downloadGroupMembersPagesByGroupId";
	/** 客户端发送的根据群组环信ID下载全部群成员信息的请求 */
	String REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID 	= 		"downloadGroupMembersByHxId";
	/** 客户端发送的根据群组环信ID分页下载群成员信息的请求 */
	String REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID_LIMIT 	= 		"downloadGroupMembersPagesByHxId";
	/** 客户端发送的删除单个群成员的请求 */
	String REQUEST_DELETE_GROUP_MEMBER 				= 		"deleteGroupMember";
	/** 客户端发送的删除多个群成员的请求 */
	String REQUEST_DELETE_GROUP_MEMBERS 			= 		"deleteGroupMembers";
	/** 客户端发送的删除群组的请求 */
	String REQUEST_DELETE_GROUP 					= 		"deleteGroup";
	/** 客户端发送的根据环信ID删除群组的请求 */
	String REQUEST_DELETE_GROUP_BY_HXID 			= 		"deleteGroupByHxid";
	/** 客户端发送的获取指定用户的群组全部数据列表的请求 */
	String REQUEST_FIND_GROUP_BY_USER_NAME 			= 		"findAllGroupByUserName";
	/** 客户端发送的分页下载公开群组列表的请求 */
	String REQUEST_FIND_PUBLIC_GROUPS 				= 		"findPublicGroups";
	/** 客户端发送的根据群组名称模糊查询群组信息的请求 */
	String REQUEST_FIND_GROUP_BY_GROUP_NAME 		= 		"findGroupByGroupName";
	/** 客户端发送的根据群组ID查找群组信息的请求 */
	String REQUEST_FIND_GROUP_BY_ID					= 		"findGroupByGroupId";
	/** 客户端发送的根据群组环信ID查找群组信息的请求 */
	String REQUEST_FIND_GROUP_BY_HXID 				= 		"findGroupByHxId";
	/** 客户端发送的根据群组环信id查找公开群组的请求 */
	String REQUEST_FIND_PUBLIC_GROUP_BY_HXID 		= 		"findPublicGroupByHxId";
	/** 客户端发送的上传用户地理位置信息的请求 */
	String REQUEST_UPLOAD_LOCATION 					= 		"uploadLocation";
	/** 客户端发送的更新用户地理位置信息的请求 */
	String REQUEST_UPDATE_LOCATION 					= 		"updateLocation";
	/** 客户端发送的分页下载附近的人的请求 */
	String REQUEST_DOWNLOAD_LOCATION 				= 		"downloadLocation";
	/** 客户端发送的全部礼物信息并展示，包括礼物的名称、图片地址和价格的请求 */
	String REQUEST_ALL_GIFTS		 				= 		"live/getAllGifts";
	/** 客户端发送的分页加载充值流水的请求 */
	String REQUEST_RECHARGE_STATEMENTS_PAGE 		= 		"live/getRechargeStatements";
	/** 客户端发送的根据用户名获取账户余额的请求 */
	String REQUEST_BALANCE			 				= 		"live/getBalance";
	/** 客户端发送的统计主播收到礼物的次数、数量及礼物信息等的请求 */
	String REQUEST_ANCHOR_GIFT		 				= 		"live/getGiftStatementsByAnchor";
	/** 客户端发送的用户给主播赠送礼物的请求 */
	String REQUEST_GIVING_GIFT		 				= 		"live/givingGifts";
	/** 客户端发送的用户充值的请求 */
	String REQUEST_RECHARGE			 				= 		"live/recharge";
	/** 客户端发送的用户给主播赠送礼物的请求 */
	String REQUEST_GET_ALL_CHATROOM		 			= 		"live/getAllChatRoom";
	/** 客户端发送的创建直播室 */
	String REQUEST_CREATE_CHATROOM		 			= 		"live/createChatRoom";
	/** 客户端发送的删除直播室 */
	String REQUEST_DELETE_CHATROOM		 			= 		"live/deleteChatRoom";
	/** 客户端分页加载送礼物流水 */
	String REQUEST_GIVING_GIFT_STATEMENT			= 		"live/getGivingGiftStatements";
	/** 客户端分页加载主播收礼物流水 */
	String REQUEST_RECEIVING_GIFT_STATEMENT		 	= 		"live/getReceivingGiftStatementsServlet";

	String GIFT_THUMB_URL = "http://192.168.0.116/app.mmhzyf.com/";

}
