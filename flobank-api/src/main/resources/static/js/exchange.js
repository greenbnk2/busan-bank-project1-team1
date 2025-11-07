/////////////////////////////////////////////////////////////
// 환전 약관 동의 (step1)
/////////////////////////////////////////////////////////////
document.addEventListener("DOMContentLoaded", () => {
  const agreeAll = document.getElementById("agreeAll");
  const termChecks = document.querySelectorAll(".term-check");

  // 약관동의 요소가 없으면 skip
  if (agreeAll && termChecks.length > 0) {
    // 전체 동의 클릭 시 개별 체크 전부 토글
    agreeAll.addEventListener("change", () => {
      termChecks.forEach(chk => chk.checked = agreeAll.checked);
    });

    // 개별 체크 변화 시 전체동의 상태 업데이트
    termChecks.forEach(chk => {
      chk.addEventListener("change", () => {
        const allChecked = [...termChecks].every(box => box.checked);
        agreeAll.checked = allChecked;
      });
    });
  }


  /////////////////////////////////////////////////////////////
  // 환전 예상 금액 확인 (step2)
  /////////////////////////////////////////////////////////////
  const calculateBtn = document.getElementById("calculateBtn");
  const exchangeResult = document.getElementById("exchangeResult");
  const amountInput = document.getElementById("exchangeAmount");
  const currencySelect = document.getElementById("currencySelect");

  // step2 요소가 있을 때만 실행
  if (calculateBtn && exchangeResult && amountInput && currencySelect) {
    calculateBtn.addEventListener("click", () => {
      const amount = parseFloat(amountInput.value);
      const currency = currencySelect.value;

      if (!amount || amount <= 0) {
        alert("환전 금액을 입력해주세요.");
        return;
      }

      const rates = {
        USD: 1470.59,
        JPY: 9.62,
        EUR: 1600.12,
        CNY: 203.14
      };

      const rate = rates[currency];
      const appliedRate = (rate * 0.985).toFixed(2); // 1.5% 우대
      const won = (amount * appliedRate).toLocaleString();

      document.getElementById("result-won").innerText = `${won}원`;
      document.getElementById("result-rate").innerText = `${rate.toLocaleString()}원`;
      document.getElementById("result-applied").innerText = `${appliedRate}원 (우대율 1.5%)`;

      exchangeResult.style.display = "block";
      exchangeResult.scrollIntoView({ behavior: "smooth", block: "center" });
    });
  }
});
