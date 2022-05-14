package joot.m2.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import joot.m2.client.image.Images;
import joot.m2.client.util.DrawableUtil;
import joot.m2.client.util.FontUtil;

/**
 * 修改密码
 * 
 * @author linxing
 *
 */
public class ModifyPswPane extends WidgetGroup {

	private Image bg;
	/** 提交 */
	private ImageButton btnCommit;
	/** 取消 */
	private ImageButton btnCancel;
	/** 用户名 */
	private TextField txtUna;
	/** 原始密码 */
	private TextField txtPswO;
	/** 密码 */
	private TextField txtPsw;
	/** 确认密码 */
	private TextField txtPsw1;
	private OperationConsumer closeConsumer;

	@FunctionalInterface
	public interface OperationConsumer {
		void op();
	}

	/**
	 * 
	 * @param closeConsumer 关闭面板时执行的操作
	 */
	public ModifyPswPane(OperationConsumer closeConsumer) {
		this.closeConsumer = closeConsumer;
	}

	private boolean lastVisible = true;

	@Override
	public void act(float delta) {
		initializeComponents();
		if (isVisible() && !lastVisible) {
			getStage().setKeyboardFocus(txtUna);
		}
		lastVisible = isVisible();

		super.act(delta);
	}

	private boolean inited;
	private boolean initializeComponents() {
		if (inited)
			return true;
		var texs = Images.get("prguse/50", "prguse/52", "prguse/81");
		if (texs == null)
			return false;

		addActor(bg = new Image(texs[0]));

		addActor(btnCancel = new ImageButton(
				new ImageButtonStyle(new TextureRegionDrawable(texs[1]), null, null, null, null, null)));
		addActor(btnCommit = new ImageButton(
				new ImageButtonStyle(null, new TextureRegionDrawable(texs[2]), null, null, null, null)));

		addActor(txtUna = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored, Color.WHITE,
				DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtUna.setWidth(130);
		txtUna.setMaxLength(18);
		txtUna.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c));
		addActor(txtPswO = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored, Color.WHITE,
				DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtPswO.setWidth(130);
		txtPswO.setMaxLength(20);
		txtPswO.setPasswordMode(true);
		txtPswO.setPasswordCharacter('*');
		txtPswO.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.'
				|| c == '_' || c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!'
				|| c == '~' || c == '`');
		addActor(txtPsw = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored, Color.WHITE,
				DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtPsw.setWidth(130);
		txtPsw.setMaxLength(20);
		txtPsw.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.' || c == '_'
				|| c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!' || c == '~'
				|| c == '`');
		addActor(txtPsw1 = new TextField("", new TextFieldStyle(FontUtil.Song_12_all_colored, Color.WHITE,
				DrawableUtil.Cursor_White, DrawableUtil.Bg_LightGray, null)));
		txtPsw1.setWidth(130);
		txtPsw1.setMaxLength(20);
		txtPsw1.setTextFieldFilter((t, c) -> Character.isLetterOrDigit(c) || c == '@' || c == '$' || c == '.'
				|| c == '_' || c == '-' || c == '*' || c == '^' || c == '%' || c == '&' || c == '#' || c == '!'
				|| c == '~' || c == '`');

		btnCancel.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (closeConsumer != null)
					closeConsumer.op();
			}

		});

		bg.setPosition((getStage().getWidth() - bg.getWidth()) / 2, (getStage().getHeight() - bg.getHeight()) / 2);
		btnCommit.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 181, (getStage().getHeight() - bg.getHeight()) / 2 + 13.5f);
		btnCancel.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 276, (getStage().getHeight() - bg.getHeight()) / 2 + 14.5f);
		txtUna.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 240, (getStage().getHeight() - bg.getHeight()) / 2 + 168);
		txtPswO.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 240, (getStage().getHeight() - bg.getHeight()) / 2 + 135);
		txtPsw.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 240, (getStage().getHeight() - bg.getHeight()) / 2 + 108);
		txtPsw1.setPosition((getStage().getWidth() - bg.getWidth()) / 2 + 240, (getStage().getHeight() - bg.getHeight()) / 2 + 77);

		inited = true;
		return true;
	}
}
