# PsdEditText
前言：接到需求需要做支付管理界面，其中涉及到密码框的输入

接到这个需求的时候，首先第一个想到的是写一个EditText，写一个背景，然后自定义行间距，中间插入view即可实现。查阅资料后从这个blog中发现了怎么处理

这个是链接：Android Study 之真正解决TextView字间距，那些扯淡的边儿去吧 - CSDN博客

但是在自己处理的时候，出现了很多bug，并且行间距并不好控制，不美观，遂作罢没用。

第二个想法是写多个edittext，监听输入完成后的状态，这样也可以实现，但是代码不灵活，要考虑各种edittext的框有没有值的问题，代码量较大，遂作罢。

第三个想法，写一个Edittext，然后在其上面覆盖多个textview，根据监听Edittext，动态的添加textview的个数,将Edittext中的值设置到textview中。

开始写代码，因为涉及到覆盖，所以需要用到RelativeLayout这个控件来做到
初始化edittext

//初始化edittext
    public void initEdit(int bg)
    {
        editText = new EditText(context);
        editText.setBackgroundResource(bg);
        editText.setCursorVisible(false);
//        editText.setTextSize(0);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
//        输入框EditText限制条件setFilters
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pwdlength)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Editable etext = editText.getText();
//                把光标放在EditText中文本的末尾处
                Selection.setSelection(etext, etext.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initDatas(s);
                if(s.length() == pwdlength)
                {
                    if(onTextFinishListener != null)
                    {
                        onTextFinishListener.onFinish(s.toString().trim());
                    }
                }
            }
        });
        //添加EditText
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(editText, lp);

    }
初始化textview

/**
     * 初始化textview
     * @param bg 背景drawable
     * @param pwdlength 密码长度
     * @param slpilinewidth 分割线宽度
     * @param splilinecolor 分割线颜色
     * @param pwdcolor 密码字体颜色
     * @param pwdsize 密码字体大小
     */
    public void initTextview(int bg, int pwdlength, float slpilinewidth, int splilinecolor, int pwdcolor, int pwdsize){
        //密码框的父布局
        linearLayout = new LinearLayout(context);
        linearLayout.setBackgroundResource(bg);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(linearLayout);

        //添加密码框
        textViews = new TextView[pwdlength];
        textViews = new TextView[pwdlength];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER;

        //添加分割线
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(dip2px(context, slpilinewidth),LayoutParams.MATCH_PARENT);

        for(int i = 0; i < textViews.length; i++)
        {
            final int index = i;
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textViews[i] = textView;
            textViews[i].setTextSize(pwdsize);
            textViews[i].setTextColor(context.getResources().getColor(pwdcolor));
            textViews[i].setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(textView, params);


            if(i < textViews.length - 1)
            {
                View view = new View(context);
                view.setBackgroundColor(context.getResources().getColor(splilinecolor));
                linearLayout.addView(view, params2);
            }
        }
    }
根据Edittext的输入内容设置Textview的值

public void initDatas(Editable s)
    {
        if(s.length() > 0)
        {
            int length = s.length();
            for(int i = 0; i < pwdlength; i++)
            {
                if(i < length)
                {
                    for(int j = 0; j < length; j++)
                    {
                        char ch = s.charAt(j);
                        textViews[j].setText(String.valueOf(ch));
                    }
                }
                else
                {
                    textViews[i].setText("");
                }
            }
        }
        else
        {
            for(int i = 0; i < pwdlength; i++)
            {
                textViews[i].setText("");
            }
        }
    }
密码输入完成后的回调

public void setOnTextFinishListener(OnTextFinishListener onTextFinishListener) {
        this.onTextFinishListener = onTextFinishListener;
    }

    public interface OnTextFinishListener
    {
        void onFinish(String str);
    }
