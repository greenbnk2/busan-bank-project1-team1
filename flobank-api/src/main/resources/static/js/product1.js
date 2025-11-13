document.addEventListener("DOMContentLoaded", function () {

    /* === 통화별 입력값 저장용 === */
    const currencyAmountValues = {};

    /* === DTO 기준 name과 일치 === */

    // 예금 유형 (dpstType: 1=거치식, 2=자유적립)
    const depositTypeRadios = document.querySelectorAll('input[name="dpstType"]');
    const depositTypeSection = document.querySelector(".conditional.deposit-type");

    // 추가 납입 여부 (dpstAddPayYn)
    const extraDepositRadios = document.querySelectorAll('input[name="dpstAddPayYn"]');
    const extraDepositSection = document.querySelector(".conditional.extra-deposit");

    // 통화 선택 (dpstCurrency)
    const currencyCheckboxes = document.querySelectorAll('input[name="dpstCurrency"]');
    const currencyAmountContainer = document.getElementById("currencyAmountContainer");

    // 가입 기간 유형 (dpstPeriodType)
    const joinTypeRadios = document.querySelectorAll('input[name="dpstPeriodType"]');
    const joinTypeFree = document.querySelector('.conditional.join-type[data-type="1"]');
    const joinTypeFixed = document.querySelector('.conditional.join-type[data-type="2"]');

    // 나이 제한 여부
    const ageLimitRadios = document.querySelectorAll('input[name="ageLimit"]');
    const ageLimitSection = document.querySelector(".conditional.age-limit");

    // 분할 인출 (dpstPartWdrwYn)
    const withdrawalRadios = document.querySelectorAll('input[name="dpstPartWdrwYn"]');
    const withdrawalSection = document.querySelector(".conditional.partial-withdrawal");
    const withdrawCurrencyContainer = document.getElementById("withdrawCurrencyContainer");

    // 자동연장 (dpstAutoRenewYn)
    const autoRenewRadios = document.querySelectorAll('input[name="dpstAutoRenewYn"]');
    const autoRenewSection = document.querySelector(".conditional.auto-renew");



    /* === 초기 실행 === */
    updateAll();



    /* === 이벤트 등록 === */
    depositTypeRadios.forEach(radio => radio.addEventListener("change", updateAll));
    extraDepositRadios.forEach(radio => radio.addEventListener("change", updateExtraDeposit));
    joinTypeRadios.forEach(radio => radio.addEventListener("change", updateJoinType));
    ageLimitRadios.forEach(radio => radio.addEventListener("change", updateAgeLimit));
    withdrawalRadios.forEach(radio => radio.addEventListener("change", updateAll));
    autoRenewRadios.forEach(radio => radio.addEventListener("change", updateAutoRenew));

    currencyCheckboxes.forEach(box => {
        box.addEventListener("change", updateAll);
    });

    /* === on input: 값 저장 === */
    document.addEventListener("input", function (e) {
        if (e.target.name.startsWith("minAmount_") || e.target.name.startsWith("maxAmount_")) {
            currencyAmountValues[e.target.name] = e.target.value;
        }
    });



    /* === 전체 갱신 === */
    function updateAll() {
        updateDepositType();
        updateExtraDeposit();
        updateJoinType();
        updateAgeLimit();
        updateWithdrawal();
        updateAutoRenew();
    }



    /* === 예금 유형 === */
    function updateDepositType() {
        const selected = document.querySelector('input[name="dpstType"]:checked').value;

        if (selected === "1") {  // 거치식
            depositTypeSection.style.display = "block";
            updateCurrencyAmountFields();
        } else {
            depositTypeSection.style.display = "none";
            currencyAmountContainer.innerHTML = "";
        }
    }



    /* === 추가납입 === */
    function updateExtraDeposit() {
        const selected = document.querySelector('input[name="dpstAddPayYn"]:checked');
        extraDepositSection.style.display = (selected && selected.value === "Y") ? "block" : "none";
    }



    /* === 가입기간 유형 === */
    function updateJoinType() {
        const selected = document.querySelector('input[name="dpstPeriodType"]:checked').value;

        joinTypeFree.style.display = selected === "1" ? "block" : "none";
        joinTypeFixed.style.display = selected === "2" ? "block" : "none";
    }



    /* === 나이 제한 === */
    function updateAgeLimit() {
        const selected = document.querySelector('input[name="ageLimit"]:checked').value;
        ageLimitSection.style.display = selected === "yes" ? "block" : "none";
    }



    /* === 분할 인출 === */
    function updateWithdrawal() {
        const selected = document.querySelector('input[name="dpstPartWdrwYn"]:checked').value;
        const depositType = document.querySelector('input[name="dpstType"]:checked').value;

        withdrawCurrencyContainer.innerHTML = "";

        if (selected === "N") {
            withdrawalSection.style.display = "none";
            return;
        }

        withdrawalSection.style.display = "block";

        if (depositType === "1") {
            updateCurrencyWithdrawFields();
        }
    }



    /* === 자동연장 === */
    function updateAutoRenew() {
        const selected = document.querySelector('input[name="dpstAutoRenewYn"]:checked').value;
        autoRenewSection.style.display = selected === "Y" ? "block" : "none";
    }



    /* === 통화별 최소/최대 금액 === */
    function updateCurrencyAmountFields() {

        const selectedType = document.querySelector('input[name="dpstType"]:checked').value;
        if (selectedType !== "1") return;

        currencyAmountContainer.innerHTML = "";
        const checkedCurrencies = Array.from(currencyCheckboxes).filter(box => box.checked);

        checkedCurrencies.forEach(box => {
            const code = box.value;
            const label = box.parentElement.textContent.trim();

            const minKey = "minAmount_" + code;
            const maxKey = "maxAmount_" + code;

            const wrapper = document.createElement("div");
            wrapper.classList.add("currency-amount-block");

            wrapper.innerHTML = `
                <h4>${label}</h4>
                <div class="form-inline">
                    <label>최소 가입액</label>
                    <input type="number"
                           name="${minKey}"
                           value="${currencyAmountValues[minKey] ?? ''}"
                           placeholder="예: 1,000" />
                </div>
                <div class="form-inline">
                    <label>최대 가입액</label>
                    <input type="number"
                           name="${maxKey}"
                           value="${currencyAmountValues[maxKey] ?? ''}"
                           placeholder="예: 1,000,000" />
                </div>
            `;

            currencyAmountContainer.appendChild(wrapper);
        });
    }



    /* === 통화별 최소 출금 금액 === */
    function updateCurrencyWithdrawFields() {

        const selectedCurrencies = Array.from(currencyCheckboxes).filter(c => c.checked);

        withdrawCurrencyContainer.innerHTML = "";

        if (selectedCurrencies.length === 0) return;

        selectedCurrencies.forEach(currency => {

            const label = currency.parentElement.textContent.trim();

            const block = document.createElement("div");
            block.classList.add("currency-amount-block");

            block.innerHTML = `
                <h4>${label} 최소 출금 금액</h4>
                <div class="form-inline">
                    <label>최소 출금금액</label>
                    <input type="number" name="minWithdraw_${currency.value}"
                        placeholder="예: ${currency.value} 최소 금액" />
                </div>
            `;

            withdrawCurrencyContainer.appendChild(block);
        });
    }

});
