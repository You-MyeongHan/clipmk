// CKEditor 초기화
function initEditor() {
    ClassicEditor
        .create(document.querySelector('#editor'), {
            toolbar: {
                items: [
                    'heading',
                    '|',
                    'bold',
                    'italic',
                    'link',
                    'bulletedList',
                    'numberedList',
                    '|',
                    'outdent',
                    'indent',
                    '|',
                    'blockQuote',
                    'insertTable',
                    'undo',
                    'redo'
                ]
            },
            language: 'ko',
            table: {
                contentToolbar: [
                    'tableColumn',
                    'tableRow',
                    'mergeTableCells'
                ]
            }
        })
        .catch(error => {
            console.error(error);
        });
}

// DOM이 로드된 후 에디터 초기화
document.addEventListener('DOMContentLoaded', initEditor); 

document.querySelector('form').addEventListener('submit', function(e) {

    const title = document.getElementById('title').value;
    const table = document.getElementById('table').value;
    const group = document.getElementById('group').value;
    const content = document.getElementById('content').value;
    const thumbnail = document.getElementById('thumbnail').value;

    this.submit();
});

// 카테고리와 서브카테고리 매핑
const categorySubCategories = {
    'HUMOR': [
        { value: 'HUMOR', label: '유머' },
        { value: 'EVENT', label: '정치' },
        { value: 'MYSTERY', label: '공포/미스테리' }
    ],
    'SPORTS': [
        { value: 'FOOTBALL', label: '축구' },
        { value: 'BASEBALL', label: '야구' },
        { value: 'BASKETBALL', label: '농구' },
        { value: 'VOLLEYBALL', label: '배구' },
        { value: 'ESPORTS', label: 'ESPORTS' },
        { value: 'OTHERSPORTS', label: '기타스포츠' }
    ],
    'MONEY': [
        { value: 'SHOPPING', label: '쇼핑' },
        { value: 'STOCK', label: '주식' },
        { value: 'ESTATE', label: '부동산' },
        { value: 'CRYPTO', label: '가상화폐' },
    ],
    'GENERAL': [
        { value: 'NOTICE', label: '공지사항' },
        { value: 'NEWS', label: '뉴스' },
        { value: 'EVENT', label: '이벤트' }
    ],
    'GAME': [
        { value: 'FCONLINE', label: 'FC온라인' },
        { value: 'LOL', label: '리그오브레전드' },
        { value: 'DF', label: '던파' },
        { value: 'MAPLE', label: '메이플' },
        { value: 'PES', label: '이펙트' },
        { value: 'FOOTBALL_MANAGER', label: '풋볼매니저' },
        { value: 'OVERWATCH', label: '오버워치' },
        { value: 'VALORANT', label: '발로란트' },
        { value: 'PUBG', label: '배틀그라운드' },
        { value: 'LOSTARK', label: '로스트아크' },
        { value: 'DIABLO', label: '디아블로' },
        { value: 'OTHERGAMES', label: '기타게임' }
    ],
    'GALLERY': [
        { value: 'TRAVEL', label: '여행' },
        { value: 'FASHION', label: '패션' },
        { value: 'FOOD', label: '음식' },
        { value: 'BEAUTY', label: '뷰티' },
        { value: 'PET', label: '반려동물' },
        { value: 'HOBBY', label: '취미' },
        { value: 'CAR', label: '자동차' },
        { value: 'INTERIOR', label: '인테리어' },
        { value: 'ART', label: '예술' },
        { value: 'NATURE', label: '자연' },
        { value: 'OTHERHOBBIES', label: '기타취미' }
    ],
    'COMMUNITY': [
        { value: 'FREE', label: '자유게시판' },
        { value: 'QNA', label: '질문과답변' },
        { value: 'HONEYTIP', label: '꿀팁' }
    ]
};

// 카테고리 변경 시 서브카테고리 업데이트
document.getElementById('category').addEventListener('change', function() {
    const subCategorySelect = document.getElementById('subCategory');
    const selectedCategory = this.value;
    
    // 서브카테고리 옵션 초기화
    subCategorySelect.innerHTML = '<option value="">서브카테고리를 선택하세요</option>';
    
    // 선택된 카테고리에 해당하는 서브카테고리 옵션 추가
    if (selectedCategory && categorySubCategories[selectedCategory]) {
        categorySubCategories[selectedCategory].forEach(subCategory => {
            const option = document.createElement('option');
            option.value = subCategory.value;
            option.textContent = subCategory.label;
            subCategorySelect.appendChild(option);
        });
    }
});