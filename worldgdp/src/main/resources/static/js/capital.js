document.addEventListener("DOMContentLoaded", () => {
	const capitalInput = document.getElementById("capitalName");
	const suggetionList = document.getElementById("capitalSuggetions");
	const capitalIdInput = document.getElementById("capitalId");

	let debounceTimer;

	if(capitalInput){
	capitalInput.addEventListener("input", function() {
		const query = this.value.trim();

		if (query.length < 3) {
			suggetionList.classList.add("d-none");
			return;
		}

		clearTimeout(debounceTimer);
		debounceTimer = setTimeout(() => {
			fetch(`/worldgdp/api/city/${countryCode}/search?query=${encodeURIComponent(query)}`)
				.then(response => response.json())
				.then(data => {

					console.log(data);
					suggetionList.innerHTML = "";

					if (data.length === 0) {
						const li = document.createElement("li");
						li.classList.add("list-group-item", "list-group-item-action");
						li.textContent = "No City Found";
						suggetionList.appendChild(li);
						return;
					}

					data.forEach(city => {
						console.log(city.name);
						const li = document.createElement("li");
						li.classList.add("list-group-item", "list-group-item-action");
						li.textContent = `${city.name}, ${city.district}`;
						li.dataset.id = city.id;

						li.addEventListener("click", () => {
							capitalInput.value = city.name;
							capitalIdInput.value = city.id;
							suggetionList.classList.add("d-none");
						});
						suggetionList.appendChild(li);
					});

					suggetionList.classList.remove("d-none");
				});
		}, 500);
	});

	document.addEventListener("click", (e) => {
		if (!capitalIdInput.contains(e.target) && !suggetionList.contains(e.target)) {
			suggetionList.classList.add("d-none");
		}
	});
	}

});