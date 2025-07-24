<template>
    <div class="home-container">
        <div class="section">
            <div class="profile-table">
                <div class="tableTitle">
                    <h2>個人資訊</h2>
                </div>
                <table>
                    <tr>
                        <th>ID</th>
                        <td>{{ user.id }}</td>
                    </tr>
                    <tr>
                        <th>姓名</th>
                        <td>{{ user.name }}</td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td>{{ user.email }}</td>
                    </tr>
                    <tr>
                        <th>付款帳號</th>
                    <td>
                        <ul>
                            <li v-for="acc in user.accounts" :key="acc">{{ acc }}</li>
                        </ul>
                        <div style="display: flex; justify-content: space-between;">
                            <input v-model="newAccount" placeholder="新增帳號" style="width: 80%;"/>
                            <button @click="addAccount(user.id, newAccount)">新增</button>
                        </div>
                    </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="section">
            <div class="tableTitle">
                <h2>產品列表</h2>
                <button @click="addNewProduct">新增產品</button>
            </div>
            <table>
                <thead>
                    <tr>
                    <th>產品名稱</th>
                    <th>產品價格</th>
                    <th>手續費(%)</th>
                    <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(product, index) in products" :key="index">
                        <td>
                            <template v-if="product.isNew">
                                <input v-model="product.name" placeholder="產品名稱" />
                            </template>
                            <template v-else>
                                {{ product.name }}
                            </template>
                        </td>
                        <td>
                            <template v-if="product.isNew">
                                <input type="number" v-model.number="product.price" min="0" />
                            </template>
                            <template v-else>
                                {{ product.price }}
                            </template>
                        </td>
                        <td>
                            <template v-if="product.isNew">
                                <input type="number" v-model.number="product.fee" min="0" step="0.01" />
                            </template>
                            <template v-else>
                                {{ product.fee }}%
                            </template>
                        </td>
                        <td>
                            <template v-if="product.isNew">
                                <div class="button-container">
                                    <button @click="saveProduct(product)">儲存</button>
                                    <button @click="cancelProduct(index)">取消</button>
                                </div>
                            </template>
                            <template v-else>
                                <button @click="openFavoriteDialog(product)">加入喜好</button>
                            </template>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div>
        <div class="tableTitle">
            <h2>喜好清單</h2>
            <div style="display: flex; justify-content: flex-end; width: 90%; gap:10px">
                <input v-model="search.productName" placeholder="商品名稱" style="width:20%"/>
                <input v-model="search.account" placeholder="扣款帳號" style="width:20%"/>
                <input v-model="search.fee" placeholder="總手續費" style="width:20%"/>
                <input v-model="search.price" placeholder="總扣款金額" style="width:20%"/>
                <button @click="filterFavorites">查詢</button>
                <button @click="resetSearch">清除</button>
            </div>
        </div>
        <table>
            <thead>
                <tr>
                <th>產品名稱</th>
                <th>購買數量</th>
                <th>付款帳號</th>
                <th>總手續費</th>
                <th>總扣款金額</th>
                <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in favorites" :key="index">
                    <td>{{ item.name }}</td>
                    <td>{{ item.quantity }}</td>
                    <td>{{ item.paymentAccount }}</td>
                    <td>{{ item.totalFee }}</td>
                    <td>{{ item.totalAmount }}</td>
                    <td>
                        <i class="fas fa-edit icon-edit" @click="editItem(index)" title="編輯" style="cursor: pointer; margin-right: 10px;"></i>
                        <i class="fas fa-trash-alt icon-delete" @click="deleteItem(item.name, item.sn)" title="刪除" style="cursor: pointer;"></i>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <FavoriteDialog ref="dialogRef" :user-id="currentUserId" @confirm="handleConfirm" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import FavoriteDialog from './FavoriteDialog.vue'
import { useAuthStore } from '@/stores/authStore'
import { callApi } from '@/utils/api'

const products = ref([])
const favorites = ref([])
const originalFavorites = ref([])  
const dialogRef = ref(null)
const newAccount = ref('')
const search = ref({
  productName: '',
  account: '',
  fee:'',
  price:''
})

const user = ref({
  id: '',
  name: '',
  email: '',
  accounts: []
})

onMounted(async () => {
  const authStore = useAuthStore()
  const currentUserId = authStore.userId

  try {
    const { success, message } = await callApi('/api/product/all')

    if (success) {
      const data = JSON.parse(message)
      products.value = data.map(p => ({
        ...p,
        quantity: 1,
        paymentAccount: '',
        isNew: false
      }))
    } else {
      alert('載入產品列表失敗：' + message)
    }

    if (currentUserId) {
      const { success: successUser, message: msgUser } = await callApi(`/api/user/${currentUserId}`)
      if (successUser) {
        user.value = JSON.parse(msgUser)
      } else {
        alert('載入個人資料失敗：' + msgUser)
      }

      const { success: successFav, message: msgFav } = await callApi(`/api/likelist/${currentUserId}`)
      if (successFav) {
        const dataFav = JSON.parse(msgFav)
        favorites.value = dataFav
        originalFavorites.value = dataFav
      } else {
        alert('載入喜好清單失敗：' + msgFav)
      }
    }
  } catch (err) {
    alert('初始化失敗：' + err.message)
  }
})

function addNewProduct() {
  products.value.push({
    name: '',
    price: 0,
    fee: 0,
    quantity: 1,
    isNew: true
  })
}

async function saveProduct(product) {
  if (!product.name || product.price < 0 || product.fee < 0) {
    return alert('請輸入正確的產品資訊')
  }

  try {
    const { success, message } = await callApi('/api/product/add', 'POST', {
      name: product.name,
      price: product.price,
      fee: product.fee
    })

    alert(message)
    if (success) product.isNew = false

  } catch (err) {
    alert('新增產品失敗：' + err.message)
  }
}


function cancelProduct(index) {
  products.value.splice(index, 1)
}

function openFavoriteDialog(product) {
  dialogRef.value.open(product, false)
}

function handleConfirm(data) {
  favorites.value.push(data)
  originalFavorites.value.push(data)
}

async function deleteItem(name, sn) {
  if (!sn) {
    alert("資料缺少 SN，無法刪除")
    return
  }

  if (confirm(`確定要刪除「${name}」嗎？`)) {
    try {
        const { success, message } = await callApi(`/api/likelist/${sn}`, 'DELETE')
        if (success) {
        alert(message)
        window.location.reload()
        } else {
        alert('刪除失敗：' + message)
        }
    } catch (e) {
        alert('刪除失敗：' + e.message)
    }
    }
}

function editItem(index) {
  const item = favorites.value[index]
  dialogRef.value.open(item, true)
}

async function addAccount(userId, newAccount) {
  try {
    const { success, message } = await callApi('/api/useraccount/add', 'POST', {
      userId,
      account: newAccount
    })

    if (success) {
      alert(message)
      window.location.reload()
    } else {
      alert('新增帳號失敗：' + message)
    }
  } catch (e) {
    alert('新增帳號錯誤：' + e.message)
  }
}


function filterFavorites() {
  const nameFilter = search.value.productName.toLowerCase().trim()
  const accountFilter = search.value.account.toLowerCase().trim()
  const feeFilter = search.value.fee
  const priceFilter = search.value.price

  favorites.value = originalFavorites.value.filter(item => {
    const matchName = item.name.toLowerCase().includes(nameFilter)
    const matchAccount = item.paymentAccount.toLowerCase().includes(accountFilter)

    const matchFee = feeFilter === '' || Number(item.totalFee) === Number(feeFilter)
    const matchPrice = priceFilter === '' || Number(item.totalAmount) === Number(priceFilter)

    return matchName && matchAccount && matchFee && matchPrice
  })
}

function resetSearch() {
  search.value.productName = ''
  search.value.account = ''
  search.value.fee = ''
  search.value.price = ''
  favorites.value = [...originalFavorites.value]
}
</script>

<style scoped>
    .home-container {
    display: flex;
    gap: 20px;
    padding: 20px;
    align-items: flex-start;
    }

    .section {
    flex: 1;
    min-width: 300px;
    }
    .tableTitle {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    }
    table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 30px;
    }
    th, td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
    }
    input {
    width: 100%;
    padding: 5px;
    }
    button {
    padding: 5px 10px;
    background-color: #42b983;
    border: none;
    color: white;
    border-radius: 4px;
    cursor: pointer;
    }
    button:hover {
    background-color: #369f6e;
    }
    .button-container{
        display: flex;
        justify-content: space-between;
    }
    .button-container button{
        width: 45%;
    }
    .icon-edit, .icon-delete{
        color: #42b983;
    }
</style>
