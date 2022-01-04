# MAD_BIT
MADCAMP with BITCOIN
___
### 요약
#### Tab1. 연락처 탭
##### 쉽고 편리하게 사용 할 수 있는 연락처 탭이에요.
1. 화면을 스크롤하여 사람들의 연락처를 구경할 수 있어요.
2. 원하는 상대방의 레이아웃을 클릭하여 전화, 문자 및 영상통화를 할 수 있어요.
![ezgif com-gif-maker (7)](https://user-images.githubusercontent.com/90104072/148060318-d54d0041-5207-4bde-940d-404afb414c39.gif)
3. 원하는 상대방의 레아아웃을 길게 눌러서 연락처를 수정 및 삭제를 할 수 있어요.
![ezgif com-gif-maker (5)](https://user-images.githubusercontent.com/90104072/148060320-f6f92827-d5be-4183-9d72-b0bec6fb4ff2.gif)
4. 이름을 검색하여 원하는 사람의 연락처를 얻을 수 있어요.
5. + 버튼을 눌러 상대방의 연락처를 추가할 수 있어요.
![ezgif com-gif-maker (4)](https://user-images.githubusercontent.com/90104072/148060324-76fd976a-20cc-4a02-ac1a-af28ee019bdc.gif)
#### Tab2. 갤러리 탭
##### 원하는 사진을 다양한 구도로 구경할 수 있는 갤러리 탭이에요.
1. 갤러리에서 원하는 이미지를 클릭하면 슬라이딩 뷰가 나와요.
2. 오른쪽 및 왼쪽으로 드래그를 하면서 귀여운 고양이를 구경할 수 있답니다.
3. 맘에 드는 고양이가 있다면 클릭해서 크게 볼 수 있어요.
4. 사진을 확대 및 축소하여 더 자세히 볼 수 있어요.
5. 맘에 안드는 사진은 길게 눌러 삭제할 수도 있답니다.
#### Tab3. MAD_BIT 거래소 탭
##### TAB3. 비트코인을 이용하여 가상투자를 해볼 수 있는 거래소 탭이에요.
1. COINDESK라는 가상화페 거래소 사이트에서 실시간 비트코인 가격을 받아왔어요.
2. 비트코인 가격을 등락을 Real-time Graph로 볼 수 있답니다.
3. 매수 버튼을 누르면 원하는 만큼의 비트코인을 매수할 수 있어요.
4. 비트코인의 가격이 올랐다면 매도 버튼을 눌러서 익절!
5. 잔고 부족 기능도 구현해놨어요.
6. 수수료가 0.05% 있으니 함부로 거래해선 안되겠죠?
![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/90104072/148060296-ce881458-457a-4a6e-b2f7-23c899fa38b9.gif)
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/90104072/148060303-00ed9cf3-2683-4256-bbc9-7964eb3b6a9d.gif)
### 구현
ViewPager2를 이용하고 Fragment를 통해 각 탭을 구성하였습니다.
#### 연락처 탭
##### 연락처 추가
화면 우상단의 +버튼을 누르면 이름과 연락처를 입력하여 새로운 연락처를 추가할 수 있다.
```
private void saveInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.save_click, null, false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        final Button btn_save = view.findViewById(R.id.btn_save);
        final Button btn_cancel_save = view.findViewById(R.id.btn_cancel_save);
        final EditText edit_name = view.findViewById(R.id.edit_name);
        final EditText edit_number = view.findViewById(R.id.edit_number);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_name.getText().length() == 0 && edit_number.getText().length() == 0) {
                    Toast.makeText(context, "이름과 전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    String name = edit_name.getText().toString();
                    String number = edit_number.getText().toString();
                    edit_name.setText("");
                    edit_number.setText("");
                    ContactModel data = new ContactModel(name, number);
                    int lower_bound = arrayList.size();
                    for (int i = 0; i < arrayList.size(); ++i)
                        if (arrayList.get(i).getName().compareTo(name) >= 0) {
                            lower_bound = i;
                            break;
                        }
                    arrayList.add(lower_bound, data);
                    adapter.notifyItemInserted(lower_bound);
                }
                dialog.dismiss();
            }
        });
```
+버튼을 누르면 [그림 2]와 같은 dialog가 뜨는데, 여기서 새로운 연락처에 대한 정보를 입력받는다.
기존의 연락처들이 이름에 대한 오름차순으로 정렬되어 있으므로 적합한 위치를 찾아 새로운 데이터를 추가해주고, recyclerView를 업데이트한다.
##### 연락처 검색
상단의 돋보기 아이콘 오른쪽에 검색하면 한 글자를 칠때마다 입력한 텍스트를 포함하는 연락처만 찾아서 보여준다.
##### 연락처 클릭 이벤트
연락처 클릭에 이벤트를 만들어 전화, 문자, 영상통화, 수정, 삭제 기능을 구현했다.
추가된 기능이 많기 때문에 짧게 클릭했을 때와 길게 클릭했을 때 두 경우로 나누어 분류하였다.
짧게 누르면, [그림 3]과 같은 화면이 나온다. 왼쪽부터 전화, 문자, 영상통화를 할 수 있는 버튼으로 각 버튼을 누르면 새로운 뷰를 띄워 실제로 전화를 거는 것 같은 느낌을 주려 했으나, 시간이 부족하여 다음 기회로 미뤄두었다.
길게 클릭했을 때, 수정과 삭제를 할 수 있다. 먼저, 수정 버튼을 누르면 [그림 4]와 같은 화면이 나와서 연락처를 수정할 수 있다.
삭제 버튼을 누르면 연락처가 삭제된다.
#### 갤러리 탭
##### 갤러리 파일 불러오기
```getImagePath()```함수를 통해 안드로이드 갤러리의 Path를 가져왔습니다.
```
private void getImagePath() {
        // in this method we are adding all our image paths
        // in our arraylist which we have created.
        // on below line we are checking if the device is having an sd card or not.
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            // if the sd card is present we are creating a new list in
            // which we are getting our images data with their ids.
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            // on below line we are creating a new
            // string to order our images by string.
            final String orderBy = MediaStore.Images.Media._ID;
            // this method will stores all the images
            // from the gallery in Cursor
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            // below line is to get total number of images
            int count = cursor.getCount();
            // on below line we are running a loop to add
            // the image file path in our array list.
            for (int i = 0; i < count; i++) {
                // on below line we are moving our cursor position
                cursor.moveToPosition(i);
                // on below line we are getting image file path
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                // after that we are getting the image file path
                // and adding that path in our array list.
                imagePaths.add(cursor.getString(dataColumnIndex));
            }
            // calling a method to
            // prepare our recycler view.
            prepareRecyclerView();
            imageRVAdapter.notifyDataSetChanged();
            // after adding the data to our
            // array list we are closing our cursor.
            cursor.close();
        }
    }
```
RecyclerView를 이용하여 갤러리를 구현하였습니다.
ViewPager2를 이용하여 슬라이딩 갤러리를 구현하였습니다.
#### 거래소 탭
##### COINDESK 거래소에서 비트코인 실시간 정보 불러오기
``` public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";```
```
    private void parseBpiResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();
            JSONObject jsonObject = new JSONObject(body);
            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
            JSONObject usdObject = bpiObject.getJSONObject("USD");
            builder.append(usdObject.getString("rate")).append("$");
            cost_str = builder.toString();
        } catch (Exception e) {
        }
    }
```
COINDESK의 API를 사용하여 실시간 정보를 불러왔습니다.
MPAnroidChart를 이용하여 실시간 CandleChart를 구현하였습니다.
