// 모든 리소스가 로드된 후 실행
window.onload = function() {
    console.log('모든 리소스가 로드되었습니다.');
    
    // 모달 관련 요소들 초기화
    const loginModal = document.getElementById('loginModal');
    const loginButton = document.getElementById('loginButton');
    const closeModalButton = document.getElementById('closeModalButton');
    const cancelButton = document.getElementById('cancelButton');
    const submitLoginButton = document.getElementById('submitLoginButton');
    const logoutButton = document.getElementById('logoutButton');
    const navToggle = document.getElementById('navToggle');
    const navbar = document.getElementById('navbarNav');
    
    // 페이지 로드 시 인증 상태 확인
    const accessToken = localStorage.getItem('accessToken');
    const nick = localStorage.getItem('nick');
    if (accessToken) {
        const unauthenticated = document.getElementById('unauthenticated');
        const authenticated = document.getElementById('authenticated');
        const username = document.getElementById('username');

        if (unauthenticated && authenticated) {
            unauthenticated.style.display = 'none';
            authenticated.style.display = 'block';
            username.textContent = nick;
            
        }
    }
    
    console.log('로그인 버튼:', loginButton);
    console.log('로그인 모달:', loginModal);

    // 로그인 버튼 클릭 이벤트
    if (loginButton) {
        loginButton.addEventListener('click', function() {
            console.log('로그인 버튼 클릭됨');
            if (loginModal) {
                loginModal.classList.add('show');
            }
        });
    } else {
        console.error('로그인 버튼을 찾을 수 없습니다.');
    }

    // 모달 닫기 함수
    function closeModal() {
        console.log('모달 닫기 함수 호출됨');
        if (loginModal) {
            loginModal.classList.remove('show');
            console.log('모달 닫힘');
        }
    }

    // 닫기 버튼 클릭 이벤트
    if (closeModalButton) {
        closeModalButton.addEventListener('click', function() {
            console.log('닫기 버튼 클릭됨');
            closeModal();
        });
    }

    // 취소 버튼 클릭 이벤트
    if (cancelButton) {
        cancelButton.addEventListener('click', function() {
            console.log('취소 버튼 클릭됨');
            closeModal();
        });
    }

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target == loginModal) {
            console.log('모달 외부 클릭됨');
            closeModal();
        }
    });

    // 로그인 제출
    if (submitLoginButton) {
        submitLoginButton.addEventListener('click', function() {
            console.log('로그인 제출 버튼 클릭됨');
            const uid = document.getElementById('uid').value;
            const pwd = document.getElementById('pwd').value;

            console.log('로그인 시도:', uid);

            fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    uid: uid,
                    pwd: pwd
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Login failed');
                }
                return response.json();
            })
            .then(data => {
                console.log('로그인 성공');
                // 토큰 저장
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('refreshToken', data.refreshToken);
                localStorage.setItem('nick', data.nick);
                
                // UI 업데이트
                const unauthenticated = document.getElementById('unauthenticated');
                const authenticated = document.getElementById('authenticated');
                const username = document.getElementById('username');
                
                if (unauthenticated && authenticated && username) {
                    unauthenticated.style.display = 'none';
                    authenticated.style.display = 'block';
                    username.textContent = data.nick;
                }
                
                closeModal();
            })
            .catch(error => {
                console.error('로그인 실패:', error);
                alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
            });
        });
    }

    // 로그아웃
    if (logoutButton) {
        logoutButton.addEventListener('click', function() {
            console.log('로그아웃 버튼 클릭됨');
            // 토큰 제거
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            
            // UI 업데이트
            const unauthenticated = document.getElementById('unauthenticated');
            const authenticated = document.getElementById('authenticated');
            
            if (unauthenticated && authenticated) {
                unauthenticated.style.display = 'block';
                authenticated.style.display = 'none';
            }
        });
    }

    // 네비게이션 토글
    if (navToggle) {
        navToggle.addEventListener('click', function() {
            console.log('네비게이션 토글 버튼 클릭됨');
            if (navbar) {
                navbar.classList.toggle('show');
            }
        });
    }
};

// 전역 함수로 toggleNav 정의
function toggleNav() {
    console.log('toggleNav 함수 호출됨');
    const navbar = document.getElementById('navbarNav');
    if (navbar) {
        navbar.classList.toggle('show');
    }
} 