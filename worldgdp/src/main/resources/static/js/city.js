let cityId = null;
let deleteBtn = null;

document.addEventListener("DOMContentLoaded", () => {

	const deleteModalEl = document.getElementById("deleteCityModal");
	const addModalEl = document.getElementById("addCityModal");
	const saveBtn = document.getElementById("saveCityBtn");
	const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");
	const addCityBtn = document.getElementById("addCityBtn");
	
	/* ---------- DELETE MODAL ACCESSIBILITY ---------- */

	document.querySelectorAll(".modal").forEach(modal => {

	    modal.addEventListener("hide.bs.modal", () => {
	        document.activeElement.blur();
	    });

	});
	
	if (deleteModalEl) {

		deleteModalEl.addEventListener("hidden.bs.modal", () => {
			addCityBtn?.focus();
			cityId = null;
			deleteBtn = null;
		});
	}

	/* ---------- ADD CITY ---------- */

	if (saveBtn) {
		saveBtn.addEventListener("click", addCity);
	}

	/* ---------- DELETE BUTTON CLICK ---------- */

	document.addEventListener("click", (e) => {

		const btn = e.target.closest(".deleteCityBtn");
		if (!btn) return;

		cityId = btn.dataset.id;
		deleteBtn = btn;

		if (deleteModalEl) {
			new bootstrap.Modal(deleteModalEl).show();
		}

	});

	/* ---------- CONFIRM DELETE ---------- */

	if (confirmDeleteBtn) {
		confirmDeleteBtn.addEventListener("click", () => {
			if (!cityId) return;
			deleteCity(cityId, deleteBtn);
		});
	}

	/* ---------- RESET ADD FORM ---------- */

	if (addModalEl) {
		addModalEl.addEventListener("hidden.bs.modal", () => {
			document.getElementById("cityForm")?.reset();
		});
	}

});


/* ---------- ADD CITY ---------- */

function addCity() {

	toggleAddCitySpinner(true);

	const city = {
		name: document.getElementById("cityName")?.value,
		district: document.getElementById("district")?.value,
		population: document.getElementById("population")?.value
	};

	fetch(`/worldgdp/api/city/${countryCode}/cities`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(city)
	})
		.then(r => r.json())
		.then(city => {

			appendCityToList(city);

			showToast("City added successfully");

			bootstrap.Modal
				.getInstance(document.getElementById("addCityModal"))
				?.hide();

			document.getElementById("addCityBtn")?.focus();

		})
		.catch(err => {
			console.error(err);
			showToast("Error: Can't add city");
		})
		.finally(() => toggleAddCitySpinner(false));
}


/* ---------- APPEND CITY ---------- */

function appendCityToList(city) {

	const cityList = document.getElementById("cityList");
	if (!cityList) return;

	const html = `
        <li class="list-group-item">

            <p class="mb-1 fs-5 fw-bold">${city.name}</p>

            <div class="d-flex justify-content-between align-items-center">

                <div class="d-flex gap-3">
                    <small class="text-muted">${city.district}</small>
                    <small class="text-muted">/</small>
                    <small class="text-muted">${city.population}</small>
                </div>

                <button 
                    class="btn btn-sm btn-outline-danger deleteCityBtn"
                    data-id="${city.id}"
                    title="Delete City">

                    <i class="bi bi-trash3"></i>

                </button>

            </div>

        </li>
    `;

	cityList.insertAdjacentHTML("afterbegin", html);
}


/* ---------- DELETE CITY ---------- */

function deleteCity(cityId, btn) {

	fetch(`/worldgdp/api/city/${cityId}`, { method: "DELETE" })

		.then(response => {

			bootstrap.Modal
				.getInstance(document.getElementById("deleteCityModal"))
				?.hide();

			if (response.ok) {

				btn.closest("li")?.remove();

				showToast("City deleted");

			} else {

				showToast("Failed to delete city");

			}

		})

		.catch(err => {

			console.error(err);

			showToast("Internal server error");

		});

}


/* ---------- SPINNER ---------- */

function toggleAddCitySpinner(isLoading) {

	const btn = document.getElementById("saveCityBtn");
	const spinner = document.getElementById("saveCitySpinner");

	if (!btn || !spinner) return;

	btn.disabled = isLoading;

	spinner.classList.toggle("d-none", !isLoading);

}


/* ---------- TOAST ---------- */

function showToast(message) {

	const toast = document.getElementById("cityToast");
	const body = toast?.querySelector(".toast-body");

	if (!toast || !body) return;

	body.textContent = message;

	new bootstrap.Toast(toast).show();

}