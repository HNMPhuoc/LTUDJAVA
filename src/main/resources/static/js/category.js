$(document).ready(function() {
    // Load all categories
    $.ajax({
        url: 'http://localhost:8090/api/categories',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log(data); // Kiểm tra dữ liệu nhận được từ API
            let trHTML = '';
            $.each(data, function(index, item) {
                console.log(item); // Kiểm tra từng mục trong dữ liệu
                trHTML += '<tr id="category-' + item.id + '">' +
                    '<td class="categoryId">' + item.id + '</td>' +
                    '<td class="categoryName">' + item.name + '</td>' +
                    '<td>' +
                    '<a href="#" class="btn btn-primary" onclick="openEditModal(' + item.id + ')">Edit</a> | ' +
                    '<a href="#" class="btn btn-danger" onclick="deleteCategory(' + item.id + ')">Delete</a>' +
                    '</td>' +
                    '</tr>';
            });
            $('#category-table-body').html(trHTML);
        },
        error: function(xhr, status, error) {
            console.error('Lỗi khi tải danh sách danh mục:', error); // Kiểm tra lỗi nếu có
        }
    });
});
function openAddModal() {
    $('#categoryModalLabel').text('Thêm Danh mục');
    $('#categoryId').val('');
    $('#categoryName').val('');
    $('#categoryModal').modal('show');
}

function openEditModal(id) {
    $('#categoryModalLabel').text('Chỉnh sửa Danh mục');
    $.ajax({
        url: 'http://localhost:8090/api/categories/' + id,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            $('#categoryId').val(data.id);
            $('#categoryName').val(data.name);
            $('#categoryModal').modal('show');
        }
    });
}

function saveCategory() {
    var categoryId = $('#categoryId').val();
    var categoryName = $('#categoryName').val();

    var url = categoryId ? 'http://localhost:8090/api/categories/' + categoryId : 'http://localhost:8090/api/categories';
    var method = categoryId ? 'PUT' : 'POST';

    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify({ name: categoryName }),
        success: function(data) {
            $('#categoryModal').modal('hide');
            alert('Lưu danh mục thành công!');
            location.reload();
        },
        error: function() {
            alert('Lưu danh mục thất bại.');
        }
    });
}

function deleteCategory(id) {
    if (confirm('Bạn có chắc muốn xóa danh mục này?')) {
        $.ajax({
            url: 'http://localhost:8090/api/categories/' + id,
            type: 'DELETE',
            success: function() {
                alert('Xóa danh mục thành công!');
                $('#category-' + id).remove();
            },
            error: function() {
                alert('Xóa danh mục thất bại.');
            }
        });
    }
}