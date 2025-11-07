////////////////////////////////////////////////////////////////////////////

// member.terms(약관페이지)

////////////////////////////////////////////////////////////////////////////

// 전체 동의 체크박스
const agreeAll = document.getElementById('agreeAll');
const checkboxes = document.querySelectorAll('.terms-check input');

agreeAll.addEventListener('change', () => {
  checkboxes.forEach(chk => chk.checked = agreeAll.checked);
});

// 개별 체크 시 전체동의 자동 상태 반영
checkboxes.forEach(chk => {
  chk.addEventListener('change', () => {
    agreeAll.checked = [...checkboxes].every(c => c.checked);
  });
});