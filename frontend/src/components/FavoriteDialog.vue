<template>
  <dialog ref="dialogRef" class="dialog-box">
    <h3>{{ isEdit ? '編輯喜好項目' : '加入喜好' }}</h3>
    <div>
        <label>產品名稱：
            <template v-if="isEdit">
                <input v-model="form.name" />
            </template>
            <template v-else>
                <span>{{ form.name }}</span>
            </template>
        </label>
    </div>
    <div>
        <label>產品價格：
            <template v-if="isEdit">
                <input type="number" v-model.number="form.price" />
            </template>
            <template v-else>
                <span>{{ form.price }}</span>
            </template>
        </label>
    </div>
    <div>
        <label>手續費：
            <template v-if="isEdit">
                <input type="number" v-model.number="form.fee" />
            </template>
            <template v-else>
                <span>{{ form.fee }}</span>
            </template>
        </label>
    </div>


    <label>購買數量：
      <input type="number" v-model.number="form.quantity" min="1" />
    </label>

    <label>付款帳號：
      <select v-model="form.paymentAccount">
        <option disabled value="">請選擇</option>
        <option v-for="acc in accountList" :key="acc" :value="acc">{{ acc }}</option>
      </select>
    </label>

    <p>總手續費：{{ totalFee }}</p>
    <p>總扣款金額：{{ totalAmount }}</p>

    <div class="button-container">
      <button @click="confirm">{{ isEdit ? '更新' : '確認' }}</button>
      <button @click="close">取消</button>
    </div>
  </dialog>
</template>

<script setup>
import { ref, computed, defineExpose, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'

const dialogRef = ref(null)
const accountList = ref([])
const isEdit = ref(false)

const form = ref({
  sn: null,
  name: '',
  price: 0,
  fee: 0,
  quantity: 1,
  paymentAccount: ''
})

const authStore = useAuthStore()
const currentUserId = authStore.userId

onMounted(async () => {
  if (currentUserId) {
    const res = await fetch(`http://localhost:8080/api/accounts/${currentUserId}`)
    accountList.value = await res.json()
  }
})

const totalFee = computed(() => form.value.fee / 100 * (form.value.quantity * form.value.price))
const totalAmount = computed(() => (form.value.price * form.value.quantity) + (form.value.fee / 100 * (form.value.quantity * form.value.price)) )

const open = (item, editMode = false) => {
  isEdit.value = editMode
  if (editMode) {
    form.value = { ...item } 
  } else {
    form.value = {
      sn: null,
      name: item.name,
      price: item.price,
      fee: item.fee,
      quantity: 1,
      paymentAccount: ''
    }
  }
  dialogRef.value?.showModal()
}

const close = () => {
  dialogRef.value?.close()
}

const emit = defineEmits(['confirm', 'update'])

const confirm = async () => {
  if (!form.value.paymentAccount) {
    alert('請選擇付款帳號')
    return
  }

  const payload = {
    userId: currentUserId,
    sn: form.value.sn,
    name: form.value.name,
    quantity: form.value.quantity,
    paymentAccount: form.value.paymentAccount,
    totalFee: totalFee.value,
    totalAmount: totalAmount.value,
    price: form.value.price,
    fee: form.value.fee
  }
  console.log(payload);
  const url = isEdit.value
    ? "http://localhost:8080/api/likelist/update"
    : "http://localhost:8080/api/likelist/add"

  try {
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })

    const msg = await res.text()
    if (res.ok) {
      alert(isEdit.value ? '更新成功' : '加入成功')
      emit(isEdit.value ? 'update' : 'confirm', payload)
      window.location.reload()
      close()
    } else {
      alert((isEdit.value ? '更新' : '加入') + "失敗：" + msg)
    }
  } catch (e) {
    alert((isEdit.value ? '更新' : '加入') + "失敗：" + e.message)
  }
}

defineExpose({ open, close })
</script>

<style scoped>
.dialog-box {
  padding: 20px;
  border-radius: 10px;
  width: 300px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  z-index: 9999;
  border: none;
}
.dialog-box::backdrop {
  background: rgba(0, 0, 0, 0.4);
}
.dialog-box input,
.dialog-box select {
  width: 100%;
  margin: 5px 0 10px;
  padding: 5px;
}
.button-container {
  display: flex;
  gap: 10px;
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
</style>
