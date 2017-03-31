package com.easyget.terminal.base.entity;

/**
 * 会员信息
 */
public class Member {

	private Long memberId;

	/**
	 * 13位的会员账号：第1位："1" 前端注册，"2" 营业部注册，"3" 网站注册
	 * <p>
	 * "1" + 6位柜子号 + 6位顺序号
	 * <p>
	 * "2" + 4位营业部号 + 8位顺序号
	 * <p>
	 * "3" + "00" + 10位顺序号
	 */
	private Long memberAccountNo; // 会员账号
	private String memberCardId; // 会员卡ID号 edit 6.17 Long -> String
	private String memberPhone; // 会员电话
	private String memberPassword; // 会员密码
	private Integer loginMode; // 会员登录方式
	private Integer memberState; // 会员状态
	private Integer memberType; // 0 未定义类型; 1 快递员; 2 收件人; 默认是收件人.
	private String companyId; // 快递公司ID，账户类型为非快递员时，为空
	private Integer givingMoney; // 赠送金额
	private Integer rechargeMoney; // 充值金额
	private Integer overDraftMoney; // 透支金额

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Integer getGivingMoney() {
		return givingMoney;
	}

	public void setGivingMoney(Integer givingMoney) {
		this.givingMoney = givingMoney;
	}

	public Integer getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(Integer rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public Integer getOverDraftMoney() {
		return overDraftMoney;
	}

	public void setOverDraftMoney(Integer overDraftMoney) {
		this.overDraftMoney = overDraftMoney;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberCardId() {
		return memberCardId;
	}

	public void setMemberCardId(String memberCardId) {
		this.memberCardId = memberCardId;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public Integer getLoginMode() {
		return loginMode;
	}

	public Long getMemberAccountNo() {
		return memberAccountNo;
	}

	public void setMemberAccountNo(Long memberAccountNo) {
		this.memberAccountNo = memberAccountNo;
	}

	public void setLoginMode(Integer loginMode) {
		this.loginMode = loginMode;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public Integer getMemberState() {
		return memberState;
	}

	public void setMemberState(Integer memberState) {
		this.memberState = memberState;
	}

	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}
}