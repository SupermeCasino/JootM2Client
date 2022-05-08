package joot.m2.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import joot.m2.client.image.M2Texture;
import joot.m2.client.util.AssetUtil;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;

/**
 * 创建用户
 * 
 * @author linxing
 *
 */
public class NewUserPane extends WidgetGroup {

	private Image bg;
	/** 提交 */
	private ImageButton btnCommit;
	/** 取消 */
	private ImageButton btnCancel;
	/** 叉叉 */
	private ImageButton btnClose;
	/** 用户名 */
	private TextField txtUna;
	/** 密码 */
	private TextField txtPsw;
	/** 确认密码 */
	private TextField txtPsw1;
	/** 君の名は。 */
	private TextField txtName;
	/** 安全问题1 */
	private TextField txtQ1;
	/** 安全问题答案1 */
	private TextField txtA1;
	/** 安全问题2 */
	private TextField txtQ2;
	/** 安全问题答案2 */
	private TextField txtA2;
	/** 固定电话 */
	private TextField txtTelPhone;
	/** 移动电话 */
	private TextField txtiPhone;
	/** 邮箱 */
	private TextField txtMail;
	/** 提示信息 */
	private Label lblTips;
	
	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}
	
	/**
	 * 
	 * @param closeConsumer 关闭新用户创建界面的操作
	 */
	public NewUserPane(OperationConsumer closeConsumer) {
		
		AssetUtil.<M2Texture>get(texs -> {
			addActor(bg = new Image(texs[0]));
			

			addActor(btnCommit = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[1]), null, null, null, null)));
			addActor(btnCancel = new ImageButton(new ImageButtonStyle(new TextureRegionDrawable(texs[2]), null, null, null, null, null)));
			addActor(btnClose = new ImageButton(new ImageButtonStyle(null, new TextureRegionDrawable(texs[3]), null, null, null, null)));
			
		}, "prguse/63", "prguse/62", "prguse/52", "prguse/64");

		
		addActor(txtUna = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtUna.setWidth(112);
		txtUna.setMaxLength(18);
		txtUna.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c));
		txtUna.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("登陆用户名\n可以输入字母（大小写）数字\n最长18位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtPsw = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtPsw.setWidth(112);
		txtPsw.setMaxLength(20);
		txtPsw.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.' || c == '_' || c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!' || c == '~' || c == '`');
		txtPsw.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("登陆密码\n可以输入字母（大小写）数字\n以及“~`@#$%^&*_-”等符号\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtPsw1 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtPsw1.setWidth(112);
		txtPsw1.setMaxLength(20);
		txtPsw1.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.' || c == '_' || c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!' || c == '~' || c == '`');
		txtPsw1.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("再次输入密码以确认");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtName = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtName.setWidth(112);
		txtName.setMaxLength(10);
		txtName.setTextFieldFilter((t, c) -> (c >= 0x4e00)&&(c <= 0x9fbb));
		txtName.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("姓名\n只能输入中文\n最长10位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtQ1 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtQ1.setWidth(160);
		txtQ1.setMaxLength(20);
		txtQ1.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("密码找回问题1\n请认真填写\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtA1 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtA1.setWidth(160);
		txtA1.setMaxLength(20);
		txtA1.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("密码找回答案1\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtQ2 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtQ2.setWidth(160);
		txtQ2.setMaxLength(20);
		txtQ2.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("密码找回问题2\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtA2 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtA2.setWidth(160);
		txtA2.setMaxLength(20);
		txtA2.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("密码找回答案2\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtTelPhone = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtTelPhone.setWidth(112);
		txtTelPhone.setMaxLength(18);
		txtTelPhone.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		txtTelPhone.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("固定电话号码\n只能输入数字\n最长18位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtiPhone = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtiPhone.setWidth(112);
		txtiPhone.setMaxLength(18);
		txtiPhone.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
		txtiPhone.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("手机号码\n只能输入数字\n最长18位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(txtMail = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored,
				Color.WHITE,
				DrawableUtil.Cursor_White,
				DrawableUtil.Bg_LightGray,
				null)));
		txtMail.setWidth(160);
		txtMail.setMaxLength(20);
		txtMail.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '-' || c == '.' || c == '@');
		txtMail.addListener(new FocusListener() {
			
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if (focused) {
					lblTips.setText("电子邮箱地址\n最长20位");
				} else {
					lblTips.setText("");
				}
			}
			
		});
		addActor(lblTips = new Label("", new LabelStyle(FontUtil.Song_12_all_colored, Color.WHITE)));
		lblTips.setAlignment(Align.top | Align.left, Align.left);
		lblTips.setSize(200, 280);
		
		btnCancel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (closeConsumer != null) closeConsumer.op();
			}
		});
		btnClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (closeConsumer != null) closeConsumer.op();
			}
		});
	}
	
	private boolean lastVisible = true;
	@Override
	public void act(float delta) {
		if (isVisible() && !lastVisible) {
			getStage().setKeyboardFocus(txtUna);
		}
		lastVisible = isVisible();
		
		super.act(delta);
	}
	
	@Override
	public void layout() {
		bg.setPosition((getWidth() - bg.getWidth()) / 2, (getHeight() - bg.getHeight()) / 2);
		btnCommit.setPosition((getWidth() - bg.getWidth()) / 2 + 158, (getHeight() - bg.getHeight()) / 2 + 24.5f);
		btnCancel.setPosition((getWidth() - bg.getWidth()) / 2 + 446, (getHeight() - bg.getHeight()) / 2 + 21.5f);
		btnClose.setPosition((getWidth() - bg.getWidth()) / 2 + 587, (getHeight() - bg.getHeight()) / 2 + 417.5f);
		txtUna.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 344);
		txtPsw.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 323);
		txtPsw1.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 302);
		txtName.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 272);
		txtQ1.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 204);
		txtA1.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 183);
		txtQ2.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 162);
		txtA2.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 143);
		txtTelPhone.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 113);
		txtiPhone.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 92);
		txtMail.setPosition((getWidth() - bg.getWidth()) / 2 + 164, (getHeight() - bg.getHeight()) / 2 + 71);
		lblTips.setPosition((getWidth() - bg.getWidth()) / 2 + 386, (getHeight() - bg.getHeight()) / 2 + 73);
	}
	
}
