document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Product quantity selector
    const quantityInput = document.getElementById('quantity');
    const increaseBtn = document.getElementById('increase-quantity');
    const decreaseBtn = document.getElementById('decrease-quantity');
    
    if (quantityInput && increaseBtn && decreaseBtn) {
        increaseBtn.addEventListener('click', function() {
            quantityInput.value = parseInt(quantityInput.value) + 1;
        });
        
        decreaseBtn.addEventListener('click', function() {
            if (parseInt(quantityInput.value) > 1) {
                quantityInput.value = parseInt(quantityInput.value) - 1;
            }
        });
    }
    
    // Cart item quantity update
    const updateQuantityForms = document.querySelectorAll('.update-quantity-form');
    
    updateQuantityForms.forEach(form => {
        const input = form.querySelector('input[name="quantity"]');
        const increaseBtn = form.querySelector('.increase-btn');
        const decreaseBtn = form.querySelector('.decrease-btn');
        
        if (input && increaseBtn && decreaseBtn) {
            increaseBtn.addEventListener('click', function(e) {
                e.preventDefault();
                input.value = parseInt(input.value) + 1;
                form.submit();
            });
            
            decreaseBtn.addEventListener('click', function(e) {
                e.preventDefault();
                if (parseInt(input.value) > 1) {
                    input.value = parseInt(input.value) - 1;
                    form.submit();
                }
            });
        }
    });
    
    // Payment method selection
    const paymentMethods = document.querySelectorAll('.payment-method');
    const paymentMethodInput = document.getElementById('paymentMethod');
    
    if (paymentMethods.length > 0 && paymentMethodInput) {
        paymentMethods.forEach(method => {
            method.addEventListener('click', function() {
                // Remove active class from all payment methods
                paymentMethods.forEach(m => m.classList.remove('active'));
                
                // Add active class to clicked payment method
                this.classList.add('active');
                
                // Set the payment method value
                paymentMethodInput.value = this.dataset.method;
            });
        });
    }
    
    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
    
    // Fade out alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.classList.add('fade');
            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 5000);
    });
    
    // Initialize product image gallery if it exists
    const mainImage = document.getElementById('main-product-image');
    const thumbnails = document.querySelectorAll('.product-thumbnail');
    
    if (mainImage && thumbnails.length > 0) {
        thumbnails.forEach(thumbnail => {
            thumbnail.addEventListener('click', function() {
                // Set main image src to thumbnail src
                mainImage.src = this.src;
                
                // Update active thumbnail
                thumbnails.forEach(t => t.classList.remove('border-success'));
                this.classList.add('border-success');
            });
        });
    }
});