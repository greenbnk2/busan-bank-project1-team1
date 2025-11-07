///////////////////////////////////////////////////////////////////////////

// mypage js

////////////////////////////////////////////////////////////////////////////


document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.getElementById("toggleCurrencies");
  const hiddenCurrencies = document.querySelectorAll(".currency-card.hidden");

  let expanded = false;

  toggleBtn.addEventListener("click", () => {
    expanded = !expanded;

    hiddenCurrencies.forEach(card => {
      card.style.display = expanded ? "block" : "none";
    });

    toggleBtn.textContent = expanded ? "− 접기" : "+ 더보기";
  });
});




// 계좌개설 약관 동의 페이지 JS 

document.addEventListener("DOMContentLoaded", () => {
  const agreeAll = document.getElementById("agreeAll");
  const checks = document.querySelectorAll(".term-check");

  agreeAll.addEventListener("change", () => {
    checks.forEach(chk => (chk.checked = agreeAll.checked));
  });

  checks.forEach(chk => {
    chk.addEventListener("change", () => {
      agreeAll.checked = [...checks].every(c => c.checked);
    });
  });
});


document.addEventListener("DOMContentLoaded", () => {
  // 1일 이체한도 최대 버튼
  const dayLimitBtn = document.querySelectorAll(".open2-btn-limit")[0];
  const dayLimitInput = document.querySelectorAll(".open2-limit-input")[0];

  // 1회 이체한도 최대 버튼
  const onceLimitBtn = document.querySelectorAll(".open2-btn-limit")[1];
  const onceLimitInput = document.querySelectorAll(".open2-limit-input")[1];

  // 1일 이체한도 최대 클릭 → 5억으로 변경
  dayLimitBtn.addEventListener("click", () => {
    dayLimitInput.value = "500,000,000";
  });

  // 1회 이체한도 최대 클릭 → 1억으로 변경
  onceLimitBtn.addEventListener("click", () => {
    onceLimitInput.value = "100,000,000";
  });
});
