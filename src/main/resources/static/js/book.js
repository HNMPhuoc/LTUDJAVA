$(document).ready(function() {
    // Tải tất cả sách
    $.ajax({
        url: 'http://localhost:8090/api/books',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log(data); // Kiểm tra dữ liệu nhận được từ API
            let trHTML = '';
            $.each(data, function(index, item) {
                console.log(item); // Kiểm tra từng mục trong dữ liệu
                trHTML += '<tr id="book-' + item.id + '">' +
                    '<td class="bookId">' + item.id + '</td>' +
                    '<td class="bookTitle">' + item.title + '</td>' +
                    '<td class="bookAuthor">' + item.author + '</td>' +
                    '<td class="bookPrice">' + item.price + '</td>' +
                    '<td class="bookCategoryId">' + (item.category ? item.category.name : '') + '</td>' +
                    '<td>' +
                    '<a href="#" class="btn btn-primary" onclick="openEditModal(' + item.id + ')">Edit</a> | ' +
                    '<a href="#" class="btn btn-danger" onclick="deleteBook(' + item.id + ')">Delete</a>' +
                    '</td>' +
                    '</tr>';
            });
            $('#book-table-body').html(trHTML);
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi tải danh sách sách:', error); // Kiểm tra lỗi nếu có
        }
    });

    // Tải danh mục cho dropdown
    loadCategories();
});

function loadCategories() {
    $.ajax({
        url: 'http://localhost:8090/api/categories',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            let categoryDropdown = $('#bookCategoryId');
            categoryDropdown.empty();
            $.each(data, function(index, item) {
                categoryDropdown.append($('<option>', {
                    value: item.id,
                    text: item.name
                }));
            });
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi tải danh sách danh mục:', error); // Kiểm tra lỗi nếu có
        }
    });
}

function openAddModal() {
    $('#bookModalLabel').text('Thêm Sách');
    $('#bookId').val('');
    $('#bookTitle').val('');
    $('#bookAuthor').val('');
    $('#bookPrice').val('');
    $('#bookCategoryId').val('');
    $('#bookModal').modal('show');
}

function openEditModal(id) {
    $('#bookModalLabel').text('Chỉnh sửa Sách');
    $.ajax({
        url: 'http://localhost:8090/api/books/' + id,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#bookId').val(data.id);
            $('#bookTitle').val(data.title);
            $('#bookAuthor').val(data.author);
            $('#bookPrice').val(data.price);
            $('#bookCategoryId').val(data.category ? data.category.id : '');
            $('#bookModal').modal('show');
        }
    });
}

function saveBook() {
    var bookId = $('#bookId').val();
    var bookTitle = $('#bookTitle').val();
    var bookAuthor = $('#bookAuthor').val();
    var bookPrice = $('#bookPrice').val();
    var bookCategoryId = $('#bookCategoryId').val();

    var url = bookId ? 'http://localhost:8090/api/books/' + bookId : 'http://localhost:8090/api/books';
    var method = bookId ? 'PUT' : 'POST';

    var bookData = {
        title: bookTitle,
        author: bookAuthor,
        price: bookPrice,
        categoryId: bookCategoryId
    };

    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify(bookData),
        success: function(data) {
            $('#bookModal').modal('hide');
            alert('Lưu sách thành công!');
            location.reload();
        },
        error: function() {
            alert('Lưu sách thất bại.');
        }
    });
}

function deleteBook(id) {
    if (confirm('Bạn có chắc muốn xóa sách này?')) {
        $.ajax({
            url: 'http://localhost:8090/api/books/' + id,
            type: 'DELETE',
            data: id,
            success: function(data) {
                alert('Xóa sách thành công!');
                $('#book-' + id).remove();
            },
            error: function() {
                alert('Xóa sách thất bại.');
            }
        });
    }
}