package com.xuf.www.gobang.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xuf.www.gobang.R;
import com.xuf.www.gobang.ai.Ai;
import com.xuf.www.gobang.ai.BestPos;
import com.xuf.www.gobang.db.HisDao;
import com.xuf.www.gobang.db.History;
import com.xuf.www.gobang.util.DimenUtil;
import com.xuf.www.gobang.util.Manager;
import com.xuf.www.gobang.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Time:2016/8/31 12:05
 * Created By:ThatNight
 */
public class AiGameView extends View {

    private static final String TAG = "GobangView";
    private static final int CHESS_BLACK = 1;
    private static final int CHESS_WHITE = 2;
    private Paint paint;
    //机器人难度
    private int AI_DIFFICULT = 2;
    //是否已经结束
    public boolean isWin = false;
    //画布宽度
    private int panelWidth;
    //最大行数
    private static final int MAX_LINE = 15;
    //棋子的大小为棋盘格子的3/4
    private static final float size = 3 * 1.0f / 4;
    //格子高度
    private float singleHeight;
    //谁先出手
    private boolean isWhite = true;

    private int[][] chess;

    private int chessFlag = 0;  //记录上一次下棋颜色，1为黑色，2为白色，0为刚开始

    private BestPos bs;
    Ai ai = new Ai();
    private TextView textView;
    private int mUpChessX;
    private int mUpChessY;

    private Context context;
    
    private static final int LINE_COUNT = 15;
    private static final int BOARD_MARGIN = 40;

    private static final float BOARD_LINE_WIDTH_DP = 0.7f;//棋盘线宽度
    private static final float BOARD_FRAME_WIDTH_DP = 1;//棋盘框的线宽度


    private Context mContext;

    private Paint mPointPaint;//画圆点
    private Paint mLinePaint;//画线

    private float[] mBoardFramePoints;//棋盘边框
    private float[] mVerticalLinePoints;//棋盘竖线
    private float[] mHorizontalLinePoints;//棋盘横线
    private float[] mBlackPoints;//棋盘黑点
    private Bitmap mWhiteChessBitmap;
    private Bitmap mBlackChessBitmap;
    private int mLineCount;
    private float mGridWidth;
    private float mGridHeight;
    private int gameTime =0;
    private History history = new History();
    private HisDao hisDao = new HisDao();
    private Intent intent;
    private boolean isFirst =true;
    private Manager manager;
    public AiGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        init(context);
        mBlackChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_black);
        mWhiteChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_white);

    }

    public AiGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
        mBlackChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_black);
        mWhiteChessBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.chess_white);
    }

    private void init() {
        chess = new int[MAX_LINE][MAX_LINE];
        mUpChessX = mUpChessY = MAX_LINE / 2;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        if (chessFlag == 0) {        //黑棋先下
            chess[Math.round(MAX_LINE / 2)][Math.round(MAX_LINE / 2)] = CHESS_BLACK;
            chessFlag = CHESS_BLACK;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        panelWidth = width;
        singleHeight = panelWidth * 1.0f / MAX_LINE;
        setMeasuredDimension(width, width);

        calcLinePoints();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int onlyWidth = (int) (singleHeight * size);
    }

    protected void onDraw(Canvas canvas) {
//        drawBoard(canvas);
        drawLines(canvas);
        drawPiece(canvas);
    }

    /**
     * 画棋盘
     *
     * @param canvas
     */
//    private void drawBoard(Canvas canvas) {
//        paint.setColor(Color.BLACK);
//        for (int i = 0; i < MAX_LINE; i++) {
//            /*int startX = (int) singleHeight / 2;
//            int endX = (int) (panelWidth - singleHeight / 2);
//			int y = (int) ((0.5 + i) * singleHeight);
//			canvas.drawLine(startX, y, endX, y, paint); //横线
//			canvas.drawLine(y, startX, y, endX, paint);*/
//            canvas.drawLine(singleHeight / 2, singleHeight / 2 + i * singleHeight, singleHeight / 2 + (MAX_LINE - 1) * singleHeight, singleHeight / 2 + i * singleHeight, paint);
//            canvas.drawLine(singleHeight / 2 + i * singleHeight, singleHeight / 2, singleHeight / 2 + i * singleHeight, singleHeight / 2 + (MAX_LINE - 1) * singleHeight, paint);
//        }
//    }

    /**
     * 画棋子
     *
     * @param canvas
     */
    private void drawPiece(Canvas canvas) {
        int side = 0;
        if ((side = ai.isWin(chess)) != 0) {
            textView.setVisibility(View.VISIBLE);
            if (side == CHESS_BLACK) {
                textView.setText("你输了哦!");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                String format = simpleDateFormat.format(new Date());
                history.setDate(format);
                history.setMode("人机对战");
                history.setMycolor("white");
                history.setHiscolor("black");
                history.setTime(Util.gameTime+"");
                history.setCondition("失败");
                hisDao.addHistory(history);
                Toast.makeText(context, "时间："+Util.gameTime, Toast.LENGTH_SHORT).show();
                Util.gameTime=0;
                intent = new Intent("huangqihong");
                mContext.sendBroadcast(intent);
            } else if (side == CHESS_WHITE) {
                textView.setText("你赢了哦!");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                String format = simpleDateFormat.format(new Date());
                history.setDate(format);
                history.setMode("人机对战");
                history.setMycolor("white");
                history.setHiscolor("black");
                history.setTime(Util.gameTime + "");
                history.setCondition("胜利");
                if (Util.gameTime != 0)
                {
                    hisDao.addHistory(history);
                }

                Toast.makeText(context, "时间："+Util.gameTime, Toast.LENGTH_SHORT).show();
                Util.gameTime=0;
                intent = new Intent("huangqihong");
                mContext.sendBroadcast(intent);
            }
            chessFlag = 0;
            isWin = true;
        }
        for (int i = 0; i < MAX_LINE; i++) {
            for (int j = 0; j < MAX_LINE; j++) {
                float x = BOARD_MARGIN + i * mGridWidth;
                float y = BOARD_MARGIN + j * mGridHeight;
                RectF rectF = new RectF(x - 35, y - 35, x + 35, y + 35);
                if (chess[i][j] == CHESS_BLACK) {
//                    paint.setColor(Color.BLACK);
//                    canvas.drawCircle(singleHeight / 2 + i * singleHeight, singleHeight / 2 + j * singleHeight, singleHeight / 2 - 5, paint);

                    canvas.drawBitmap(mBlackChessBitmap, null, rectF, null);
                }
                if (chess[i][j] == CHESS_WHITE) {
//                    paint.setColor(Color.WHITE);
//                    canvas.drawCircle(singleHeight / 2 + i * singleHeight, singleHeight / 2 + j * singleHeight, singleHeight / 2 - 5, paint);
                    canvas.drawBitmap(mWhiteChessBitmap, null, rectF, null);
                }
                if (i == mUpChessX && j == mUpChessY) {
//                    paint.setColor(Color.RED);
//                    paint.setStyle(Paint.Style.STROKE);
//                    paint.setStrokeWidth(4);
//                    canvas.drawCircle(singleHeight / 2 + i * singleHeight, singleHeight / 2 + j * singleHeight, singleHeight / 2 - 2, paint);
//                    paint.setStyle(Paint.Style.FILL);
//                    paint.setStrokeWidth(0);
                    float coordinateX = BOARD_MARGIN + i * mGridWidth;
                    float coordinateY = BOARD_MARGIN + j * mGridHeight;
                    mPointPaint.setColor(Color.RED);
                    canvas.drawCircle(coordinateX, coordinateY, DimenUtil.dp2px(mContext, 2), mPointPaint);
                }
            }
        }
    }


    /**
     * 触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isWin) {

            } else {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Log.i(TAG, x + "\n" + y);
                if (x < -singleHeight / 2 || x > panelWidth - singleHeight / 2 || y < 0 || y > panelWidth - singleHeight / 2) {
                    Log.d(TAG, panelWidth - singleHeight / 2 + "");
                } else {
                    int indexX = (int) (x / singleHeight);
                    int indexY = (int) (y / singleHeight);
                    if (chessFlag == CHESS_BLACK && chess[indexX][indexY] == 0) {
                        chess[indexX][indexY] = CHESS_WHITE;
                        chessFlag = CHESS_WHITE;
                        invalidate();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(30);
                                    bs = ai.action(chess, AI_DIFFICULT);
                                    Log.d(TAG, "run: ");
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = bs;
                                    handler.sendMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BestPos ps = (BestPos) msg.obj;
                    chess[ps.getBestX()][ps.getBestY()] = CHESS_BLACK;
                    mUpChessX = ps.getBestX();
                    mUpChessY = ps.getBestY();
                    chessFlag = CHESS_BLACK;
                    invalidate();
                    break;
            }
        }
    };

    public void restartGame() {
        textView.setVisibility(View.INVISIBLE);
        chess = new int[MAX_LINE][MAX_LINE];
        chessFlag = 0;
        init();
        invalidate();
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getAI_DIFFICULT() {
        return AI_DIFFICULT;
    }

    public void setAI_DIFFICULT(int AI_DIFFICULT) {
        this.AI_DIFFICULT = AI_DIFFICULT;
    }



    private void init(Context context) {
        mContext = context;
        hisDao.openDb(context);
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.BLACK);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mLineCount = LINE_COUNT;

    }
    private void calcLinePoints() {
        mHorizontalLinePoints = new float[mLineCount * 4];
        mVerticalLinePoints = new float[mLineCount * 4];

        float boardWidth = getMeasuredWidth() - BOARD_MARGIN * 2;
        float boardHeight = getMeasuredHeight() - BOARD_MARGIN * 2;

        mGridWidth = boardWidth / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mVerticalLinePoints[i] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 1] = BOARD_MARGIN;
            mVerticalLinePoints[i + 2] = i * mGridWidth / 4 + BOARD_MARGIN;
            mVerticalLinePoints[i + 3] = boardHeight + BOARD_MARGIN;
        }

        mGridHeight = boardHeight / (mLineCount - 1);
        for (int i = 0; i < mLineCount * 4; i += 4) {
            mHorizontalLinePoints[i] = BOARD_MARGIN;
            mHorizontalLinePoints[i + 1] = i * mGridHeight / 4 + BOARD_MARGIN;
            mHorizontalLinePoints[i + 2] = boardWidth + BOARD_MARGIN;
            mHorizontalLinePoints[i + 3] = i * mGridHeight / 4 + BOARD_MARGIN;
        }

        float frameMargin = BOARD_MARGIN * 0.8f;
        mBoardFramePoints = new float[]{frameMargin, frameMargin, getMeasuredWidth() - frameMargin, frameMargin,//上横
                frameMargin, getMeasuredHeight() - frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin,//下横
                frameMargin, frameMargin, frameMargin, getMeasuredHeight() - frameMargin,//左竖
                getMeasuredWidth() - frameMargin, frameMargin, getMeasuredWidth() - frameMargin, getMeasuredHeight() - frameMargin};//右竖

        mBlackPoints = new float[]{3 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 3 * mGridHeight + BOARD_MARGIN,
                7 * mGridWidth + BOARD_MARGIN, 7 * mGridHeight + BOARD_MARGIN,
                3 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN,
                11 * mGridWidth + BOARD_MARGIN, 11 * mGridHeight + BOARD_MARGIN};
    }

    private void drawLines(Canvas canvas) {
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_LINE_WIDTH_DP));
        canvas.drawLines(mHorizontalLinePoints, mLinePaint);
        canvas.drawLines(mVerticalLinePoints, mLinePaint);
        mLinePaint.setStrokeWidth(DimenUtil.dp2px(mContext, BOARD_FRAME_WIDTH_DP));
        canvas.drawLines(mBoardFramePoints, mLinePaint);
    }
}
