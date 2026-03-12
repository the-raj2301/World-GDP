let deleteLangName = null;
let removeBtn = null;

document.addEventListener("DOMContentLoaded", function() {

	const languageModal = document.getElementById("languageModal");
	const saveLanguageBtn = document.getElementById("saveLanguageBtn");
	const deleteLangModal = document.getElementById("deleteLangModal");
	const confirmDeleteLangBtn = document.getElementById("confirmDeleteLangBtn");

	/* ---------- Delete Button Click ---------- */

	document.addEventListener("click", (e) => {
		const deleteLangBtn = e.target.closest(".deleteLangBtn");
		if (!deleteLangBtn) return;
		removeBtn = deleteLangBtn;
		deleteLangName = deleteLangBtn.dataset.name;
		new bootstrap.Modal(deleteLangModal).show();
	});

	/* ---------- Confirm Delete Button Click ---------- */

	confirmDeleteLangBtn?.addEventListener("click", deleteLanguage);



	/* ---------- Save language Btn ---------- */

	saveLanguageBtn?.addEventListener("click", addLanguage);

	languageModal?.addEventListener("hidden.bs.modal", () => {
		document.getElementById("languageForm").reset();
	});

});

function addLanguage() {

	const language = {
		language: document.getElementById("language")?.value,
		percentage: document.getElementById("percentage")?.value,
		isOfficial: document.getElementById("isOfficial").checked ? "T" : "F"
	};

	fetch(`/worldgdp/api/language/${countryCode}`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(language)
	})
		.then(response => {

			bootstrap.Modal
				.getInstance(document.getElementById("languageModal"))
				?.hide();

			if (!response.ok) {
				showToast("Failed to add language", "error");
				return;
			}

			showToast("Language added successfully", "success");
			appendToList(language);

		})
		.catch((e) => {
			showToast("Server error", "error");
			console.error(e);
		});
}

function appendToList(language) {

	const languageList = document.getElementById("languageList");
	if (!languageList) return;

	const languageHtml = `
	<li class="list-group-item" data-language="${language.language}">
		<p class="mb-1 fs-5 fw-bold">${language.language}</p>
		<div class="d-flex justify-content-between align-items-center">
			<div class="d-flex gap-3">

				<small>
					<span
						class="${language.isOfficial === 'T'
			? 'rounded-circle bg-success'
			: 'rounded-circle bg-danger'}"
						style="width:12px;height:12px;display:inline-block;">
					</span>
				</small>
				<small class="text-muted">/</small>
				<small class="text-muted">
					${language.percentage}%
				</small>
				
			</div>
			<a
				class="btn btn-sm btn-outline-danger deleteLangBtn"
				data-name="${language.language}"
				title="Delete Language">
				<i class="bi bi-trash3"></i>
			</a>
		</div>
	</li>`;

	languageList.insertAdjacentHTML("afterbegin", languageHtml);

}

function deleteLanguage() {
	fetch(`/worldgdp/api/language/${countryCode}/${deleteLangName}`, { method: "DELETE" })
		.then(response => {

			bootstrap.Modal.getInstance(document.getElementById("deleteLangModal"))?.hide();

			if (!response.ok) {
				showToast(`Failed to delete language: ${deleteLangName}`, "error");
				return;
			}
			showToast(`Deleted the language: ${deleteLangName}`, "success");
			removeBtn.closest("li")?.remove();
		}).catch(e => {
			console.log(e);
			showToast("Server Error", "error");
		}).finally(() => {
			deleteLangName = null;
			removeBtn = null;
		});

}