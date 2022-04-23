package pack.meaage;

public enum MessageType {
	LOGIN_REQUEST((byte) 0,LoginRequestMessage.class),
	LOGIN_RESPONSE((byte) 1, LoginResponseMessage.class),

	CHAT_REQUEST((byte) 2, ChatRequestMessage.class),
	CHAT_RESPONSE((byte) 3, ChatResponseMessage.class),

	GROUP_CREATE_REQUEST((byte) 4, GroupCreateRequestMessage.class),
	GROUP_CREATE_RESPONSE((byte) 5, GroupCreateResponseMessage.class),

	GROUP_JOIN_REQUEST((byte)6, GroupJoinRequestMessage.class),
	GROUP_JOIN_RESPONSE((byte) 7, GroupJoinResponseMessage.class),

	GROUP_QUIT_REQUEST((byte) 8, GroupQuitRequestMessage.class),
	GROUP_QUIT_RESPONSE((byte) 9, GroupQuitResponseMessage.class),

	GROUP_CHAT_REQUEST((byte) 10, GroupChatRequestMessage.class),
	GROUP_CHAT_RESPONSE((byte) 11, GroupChatResponseMessage.class),

	GROUP_MEMBERS_REQUEST((byte) 12, GroupMembersRequestMessage.class),
	GROUP_MEMBERS_RESPONSE((byte) 13, GroupMembersResponseMessage.class),
	PING_MESSAGE((byte)14,PingMessage.class)
	;


	final byte code;
	final Class<? extends Message> clazz;

	MessageType(byte code, Class<? extends Message> check) {
		this.code = code;
		this.clazz=check;
	}

	public byte code() {
		return code;
	}

	public Class<? extends Message> getClazz() {
		return clazz;
	}
}